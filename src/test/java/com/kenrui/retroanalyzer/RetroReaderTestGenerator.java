package com.kenrui.retroanalyzer;

import com.kenrui.retroanalyzer.database.compositekeys.TimeCorrelationId;
import com.kenrui.retroanalyzer.database.compositekeys.TimePointId;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RetroReaderTestGenerator {
    private String filenameCorrelationPointIds;
    private String filenamePointIds;
    private String delimiter, tickIdFieldName, quoteIdFieldName, timeFieldName, pointIdFieldName;
    private BufferedReader bufferedReader;
    private List<List<String>> mockRetroFileContent;
    private int lineCountCorrelationIds, lineCountPointIds;

    private ArrayList<ArrayList<String>> listOfOneTickToMultipleQuotes;
    private ArrayList<ArrayList<String>> listOfOneTickToOneQuote;
    private ArrayList<ArrayList<String>> listOfDifferentTicksToSameQuote;
    private ArrayList<ArrayList<String>> listOfOneTickToNoQuote;
    private List<TimeCorrelationId> listOfIdsOneTickToMultipleQuotes;
    private List<TimeCorrelationId> listOfIdsOneTickToOneQuote;
    private List<TimeCorrelationId> listOfIdsDifferentTicksToSameQuote;
    private List<TimeCorrelationId> listOfIdsOneTickToNoQuote;

    private ArrayList<ArrayList<String>> listOfPointIds;

    private List<TimePointId> listOfIdsPointIds;

    public RetroReaderTestGenerator(String filenameCorrelationPointIds, String filenamePointIds, String delimiter, String tickIdFieldName, String quoteIdFieldName, String timeFieldName, String pointIdFieldName) {
        this.filenameCorrelationPointIds = filenameCorrelationPointIds;
        this.filenamePointIds = filenamePointIds;
        this.delimiter = delimiter;
        this.tickIdFieldName = tickIdFieldName;
        this.quoteIdFieldName = quoteIdFieldName;
        this.timeFieldName = timeFieldName;
        this.pointIdFieldName = pointIdFieldName;
    }

    public int getLineCountCorrelationIds() {
        return lineCountCorrelationIds;
    }

    public int getLineCountPointIds() {
        return lineCountPointIds;
    }

    public List<TimeCorrelationId> getListOfIdsOneTickToMultipleQuotes() {
        return listOfIdsOneTickToMultipleQuotes;
    }

    public List<TimeCorrelationId> getListOfIdsOneTickToOneQuote() {
        return listOfIdsOneTickToOneQuote;
    }

    public List<TimeCorrelationId> getListOfIdsDifferentTicksToSameQuote() {
        return listOfIdsDifferentTicksToSameQuote;
    }

    public List<TimeCorrelationId> getListOfIdsOneTickToNoQuote() {
        return listOfIdsOneTickToNoQuote;
    }

    public List<TimePointId> getListOfIdsPointIds() {
        return listOfIdsPointIds;
    }

    public void generateCorrelationIds() {
        // Generate new retro file and check number of lines added
        try {
            FileWriter fileWriter = new FileWriter(filenameCorrelationPointIds);

            // Same INPUT_EVENT_ID triggers different OUTPUT_EVENT_IDs
            listOfIdsOneTickToMultipleQuotes = new ArrayList<>();
            listOfIdsOneTickToMultipleQuotes.add(new TimeCorrelationId(getTime(), "1084123482143", "40082349"));
            listOfIdsOneTickToMultipleQuotes.add(new TimeCorrelationId(getTime(), "1084123482143", "40082350"));
            listOfIdsOneTickToMultipleQuotes.add(new TimeCorrelationId(getTime(), "1084123482143", "40082351"));
            listOfIdsOneTickToMultipleQuotes.add(new TimeCorrelationId(getTime(), "1084123482144", "40082352"));
            listOfIdsOneTickToMultipleQuotes.add(new TimeCorrelationId(getTime(), "1084123482144", "40082353"));

            listOfOneTickToMultipleQuotes = new ArrayList();
            correlationIdToLineEntries(listOfIdsOneTickToMultipleQuotes, listOfOneTickToMultipleQuotes);


            // Each INPUT_EVENT_ID triggers one OUTPUT_EVENT_ID
            listOfIdsOneTickToOneQuote = new ArrayList<>();
            listOfIdsOneTickToOneQuote.add(new TimeCorrelationId(getTime(), "1084123482145", "40082354"));
            listOfIdsOneTickToOneQuote.add(new TimeCorrelationId(getTime(), "1084123482146", "40082355"));

            listOfOneTickToOneQuote = new ArrayList();
            correlationIdToLineEntries(listOfIdsOneTickToOneQuote, listOfOneTickToOneQuote);


            // Different INPUT_EVENT_ID triggering repeating OUTPUT_EVENT_ID
            // This case is not expected and is here to test if these unexpected circumstances can happen
            listOfIdsDifferentTicksToSameQuote = new ArrayList<>();
            listOfIdsDifferentTicksToSameQuote.add(new TimeCorrelationId(getTime(), "1084123482147", "40082356"));
            listOfIdsDifferentTicksToSameQuote.add(new TimeCorrelationId(getTime(), "1084123482148", "40082356"));

            listOfDifferentTicksToSameQuote = new ArrayList();
            correlationIdToLineEntries(listOfIdsDifferentTicksToSameQuote, listOfDifferentTicksToSameQuote);


            // Each INPUT_EVENT_ID triggering some update but no quote
            listOfIdsOneTickToNoQuote = new ArrayList<>();
            listOfIdsOneTickToNoQuote.add(new TimeCorrelationId(getTime(), "1084123482149", ""));
            listOfIdsOneTickToNoQuote.add(new TimeCorrelationId(getTime(), "1084123482150", ""));

            listOfOneTickToNoQuote = new ArrayList();
            correlationIdToLineEntries(listOfIdsOneTickToNoQuote, listOfOneTickToNoQuote);

            mockRetroFileContent = new ArrayList<>();
            mockRetroFileContent.addAll(listOfOneTickToMultipleQuotes);
            mockRetroFileContent.addAll(listOfOneTickToOneQuote);
            mockRetroFileContent.addAll(listOfDifferentTicksToSameQuote);
            mockRetroFileContent.addAll(listOfOneTickToNoQuote);

            for (List<String> row : mockRetroFileContent) {
                fileWriter.append(String.join(delimiter, row));
                fileWriter.append("\n");
            }

            fileWriter.flush();
            fileWriter.close();

            FileReader fileReader = new FileReader(new File(filenameCorrelationPointIds));
            bufferedReader = new BufferedReader(fileReader);
            while (bufferedReader.readLine() != null) {
                lineCountCorrelationIds++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generatePointIds() {
        // Generate new retro file and check number of lines added
        try {
            FileWriter fileWriter = new FileWriter(filenamePointIds);

            // Same INPUT_EVENT_ID triggers different OUTPUT_EVENT_IDs
            listOfIdsPointIds = new ArrayList<>();
            listOfIdsPointIds.add(new TimePointId(getTime(), "40082349"));
            listOfIdsPointIds.add(new TimePointId(getTime(), "40082350"));
            listOfIdsPointIds.add(new TimePointId(getTime(), "40082351"));
            listOfIdsPointIds.add(new TimePointId(getTime(), "40082352"));
            listOfIdsPointIds.add(new TimePointId(getTime(), "40082353"));

            listOfPointIds = new ArrayList();
            pointIdToLineEntries(listOfIdsPointIds, listOfPointIds);

            mockRetroFileContent = new ArrayList<>();
            mockRetroFileContent.addAll(listOfPointIds);

            for (List<String> row : mockRetroFileContent) {
                fileWriter.append(String.join(delimiter, row));
                fileWriter.append("\n");
            }

            fileWriter.flush();
            fileWriter.close();

            FileReader fileReader = new FileReader(new File(filenamePointIds));
            bufferedReader = new BufferedReader(fileReader);
            while (bufferedReader.readLine() != null) {
                lineCountPointIds++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getTime() {
        return getCurrentLocalDateTimeStamp();
    }

    public static String getCurrentLocalDateTimeStamp() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }

    private void correlationIdToLineEntries(List<TimeCorrelationId> listOfTimeCorrelationIds, ArrayList<ArrayList<String>> listOfLineEntries) {
        for (TimeCorrelationId timeCorrelationId : listOfTimeCorrelationIds) {
            String tickId = tickIdFieldName + "=" + timeCorrelationId.getId1();
            String quoteId = quoteIdFieldName + "=" + timeCorrelationId.getId2();
            String time = timeFieldName + "=" + timeCorrelationId.getTime();

            listOfLineEntries.add(
                    new ArrayList(Arrays.asList(time, "IP.SRC=10.223.3.7", "IP.DST=10.223.3.8", tickId, quoteId))
            );

        }
        ;
    }

    private void pointIdToLineEntries(List<TimePointId> listOfTimePointIds, ArrayList<ArrayList<String>> listOfLineEntries) {
        for (TimePointId timePointId : listOfTimePointIds) {
            String pointId = pointIdFieldName + "=" + timePointId.getId();
            String time = timeFieldName + "=" + timePointId.getTimep();

            listOfLineEntries.add(
                    new ArrayList(Arrays.asList(time, "IP.SRC=10.223.3.7", "IP.DST=10.223.3.8", pointId))
            );

        }
        ;
    }
}
