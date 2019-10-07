package com.kenrui.retroanalyzer.reader;

import com.kenrui.retroanalyzer.database.compositekeys.TimeCorrelationId;
import com.kenrui.retroanalyzer.database.compositekeys.TimePointId;
import com.kenrui.retroanalyzer.database.entities.Correlation;
import com.kenrui.retroanalyzer.database.entities.Point;
import com.kenrui.retroanalyzer.database.repositories.PointRespository;

import java.io.*;
import java.util.List;

public class RetroReaderPointIds implements IRetroReaderPointIds {
    private PointRespository pointRespository;
    private File file;
    private String point;
    private String time, id;
    private String delimiter;
    private int duplicateKey;
    private int uniqueKey;
    private DecodedMessages decodedMessages;
    private MessageContents messageContents;

    public RetroReaderPointIds(File file, String delimiter, String point, String time, String id, PointRespository pointRespository) {
        this.file = file;
        this.delimiter = delimiter;
        this.point = point;
        this.time = time;
        this.id = id;
        this.duplicateKey = 0;
        this.uniqueKey = 0;
        this.decodedMessages = new DecodedMessages();
        this.pointRespository = pointRespository;
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
                String id1Parsed = messageContents.getField(id) == null ? "null" : messageContents.getField(id);
                TimePointId timePointId = new TimePointId(timeParsed, id1Parsed);
                Point point = new Point(timePointId, this.point, messageContents.toString().substring(1, Math.min(messageContents.toString().length(), 3000)));

                pointRespository.save(point);}

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public DecodedMessages getDecodedMessages() {
        return decodedMessages;
    }

    public List<Point> getAll() {
        return pointRespository.findAll();
    }

    @Override
    public List<TimePointId> getListOfPointIds() {
        return pointRespository.findPointIds();
    }

    @Override
    public long getLinesRead() {
        return pointRespository.count();
    }
}
