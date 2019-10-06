package com.kenrui.retroanalyzer.reader;

import com.kenrui.retroanalyzer.database.compositekeys.TimeCorrelationId;
import com.kenrui.retroanalyzer.database.compositekeys.TimePointId;
import com.kenrui.retroanalyzer.database.entities.Correlation;
import com.kenrui.retroanalyzer.database.entities.RepeatingCorrelationIdGroups;
import com.kenrui.retroanalyzer.database.repositories.CorrelationRepository;

import java.io.*;
import java.util.*;

public class RetroReaderCorrelationIds implements IRetroReaderCorrelationIds {
    private CorrelationRepository correlationRepository;
    private File file;
    private String point;
    private String time, id1, id2;
    private String delimiter;
    private int duplicateKey;
    private int uniqueKey;
    private DecodedMessages decodedMessages;
    private MessageContents messageContents;

    public RetroReaderCorrelationIds(String filename, String delimiter, String point, String time, String id1, String id2, CorrelationRepository correlationRepository) {
        this.file = new File(filename);
        this.delimiter = delimiter;
        this.point = point;
        this.time = time;
        this.id1 = id1;
        this.id2 = id2;
        this.duplicateKey = 0;
        this.uniqueKey = 0;
        this.decodedMessages = new DecodedMessages();
        this.correlationRepository = correlationRepository;
    }

    public void parseFile() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String lineRead;

            while ((lineRead = bufferedReader.readLine()) != null) {
                String[] lineArray = lineRead.split(delimiter);
                messageContents = new MessageContents();

                for (String fieldValuePair : lineArray) {
                    String[] fieldValueArray = fieldValuePair.split("=");
                    String key = "", value = "";
                    if (fieldValueArray.length == 0) {
                        // Break if there is no field value pair
                        break;
                    } else if (fieldValueArray.length == 1) {
                        // This is when there is a field but no value
                        key = fieldValueArray[0];
                    } else if (fieldValueArray.length == 2) {
                        // This is when there is a field value pair
                        key = fieldValueArray[0];
                        value = fieldValueArray[1];
                    } else {
                        // Break if there are more than 2 items
                        break;
                    }

                    messageContents.addFieldValuePair(key, value);
                }

                String timeParsed = messageContents.getField(time) == null ? "null" : messageContents.getField(time);
                String id1Parsed = messageContents.getField(id1) == null ? "null" : messageContents.getField(id1);
                String id2Parsed = messageContents.getField(id2) == null ? "null" : messageContents.getField(id2);
                TimeCorrelationId timeCorrelationId = new TimeCorrelationId(timeParsed, id1Parsed, id2Parsed);

                Correlation correlation = new Correlation(timeCorrelationId, this.point, messageContents.toString().substring(1, Math.min(messageContents.toString().length(), 3000)));

                correlationRepository.save(correlation);
            }

            correlationRepository.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Correlation> getAll() {
        return correlationRepository.findAll();
    }

    @Override
    public List<TimeCorrelationId> getListOfCorrelationIdsForOneTickToMultipleQuotes() {
        return correlationRepository.findCorrelationIdsForOneTickToMultipleQuotes();
    }

    @Override
    public List<TimeCorrelationId> getListOfCorrelationIdsForOneTickToOneQuote() {
        return correlationRepository.findCorrelationIdsForOneTickToOneQuote();
    }

    @Override
    public List<TimeCorrelationId> getListOfCorrelationIdsForDifferentTicksToSameQuote() {
        String message = "Different ticks found to be generating same quote!";
        return correlationRepository.findCorrelationIdsForDifferentTicksToSameQuote();
    }

    @Override
    public List<TimeCorrelationId> getListOfCorrelationIdsForOneTickToNoQuote() {
        return correlationRepository.findCorrelationIdsForOneTickToNoQuote();
    }

    @Override
    public long getLinesRead() {
        return correlationRepository.count();
    }

    public List<TimeCorrelationId> findCorrelationIdsNotInPoint() {
        return correlationRepository.findCorrelationIdsNotInPoint();
    }

    public Map<TimeCorrelationId,TimePointId> correlatePoints() {
        Map<TimeCorrelationId,TimePointId> mapOfCorrelatedPoints = new HashMap<>();

        List<Object[]> results = correlationRepository.correlatePoints();
        results.stream().forEach((record) -> {
            TimeCorrelationId timeCorrelationId = (TimeCorrelationId)record[0];
            TimePointId timePointId = (TimePointId)record[1];
            mapOfCorrelatedPoints.put(timeCorrelationId, timePointId);
        });
        return mapOfCorrelatedPoints;
    }

    public List<TimeCorrelationId> correlatedPoints() {
        return correlationRepository.correlatedPoints();
    }

    public List<TimeCorrelationId> findCorrelations() {
        return correlationRepository.findCorrelations();
    }

    public List<RepeatingCorrelationIdGroups> findRepeatingCorrelationIdGroups() {
        return correlationRepository.findRepeatingCorrelationIdGroups();
    }

    public Integer findNumberOfRepeatingCorrelationIdGroups() {
        return correlationRepository.findNumberOfRepeatingCorrelationIdGroups();
    }

    public Integer findNumberOfPointsFoundFromRepeatingCorrelationIdGroups() {
        return correlationRepository.findNumberOfPointsFoundFromRepeatingCorrelationIdGroups();
    }

    public Integer checkIfAllOneTickToMultipleQuotesAreCorrelatable() {
        return correlationRepository.checkIfAllOneTickToMultipleQuotesAreCorrelatable();
    }

    public Integer checkIfAllOneTickToOneQuoteAreCorrelatable() {
        return correlationRepository.checkIfAllOneTickToOneQuoteAreCorrelatable();
    }

}
