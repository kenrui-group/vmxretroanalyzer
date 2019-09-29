package com.kenrui.retroanalyzer;

import com.kenrui.retroanalyzer.database.compositekeys.TimePointId;
import com.kenrui.retroanalyzer.reader.RetroReaderPointIds;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//https://spring.io/blog/2016/04/15/testing-improvements-in-spring-boot-1-4
@SpringBootTest(classes = Application.class)
public class RetroReaderPointIdsTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private RetroReaderPointIds retroReaderPointIds;

    private String filename = "C:\\Users\\Titus\\Desktop\\point5wa.txt";
    private BufferedReader bufferedReader;
    private List<List<String>> mockRetroFileContent;
    private int lineCount;

    private ArrayList<ArrayList<String>> listOfPointIds;
    private List<TimePointId> listOfIdsPointIds;


    // SUT
    private RetroReaderPointIds retroReaderPointIdsMock;

    @BeforeClass
    public void setup() {

        // Generate new retro file and check number of lines added
        try {
            FileWriter fileWriter = new FileWriter(filename);

            // Same INPUT_EVENT_ID triggers different OUTPUT_EVENT_IDs
            listOfIdsPointIds = new ArrayList<>();
            listOfIdsPointIds.add(new TimePointId(getTime(), "1084123482143"));
            listOfIdsPointIds.add(new TimePointId(getTime(), "1084123482143"));
            listOfIdsPointIds.add(new TimePointId(getTime(), "1084123482143"));
            listOfIdsPointIds.add(new TimePointId(getTime(), "1084123482144"));
            listOfIdsPointIds.add(new TimePointId(getTime(), "1084123482144"));

            listOfPointIds = new ArrayList();
            correlationIdToLineEntries(listOfIdsPointIds, listOfPointIds);

            mockRetroFileContent = new ArrayList<>();
            mockRetroFileContent.addAll(listOfPointIds);

            for (List<String> row : mockRetroFileContent) {
                fileWriter.append(String.join(",", row));
                fileWriter.append("\n");
            }

            fileWriter.flush();
            fileWriter.close();

            FileReader fileReader = new FileReader(new File(filename));
            bufferedReader = new BufferedReader(fileReader);
            while (bufferedReader.readLine() != null) {
                lineCount++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read in test file specifying field name OUTPUT_EVENT_ID as key
        retroReaderPointIdsMock = Mockito.spy(retroReaderPointIds);
        retroReaderPointIdsMock.parseFile();
    }

    private String getTime() {
        return getCurrentLocalDateTimeStamp();
    }

    public String getCurrentLocalDateTimeStamp() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }

    private void correlationIdToLineEntries(List<TimePointId> listOfTimePointIds, ArrayList<ArrayList<String>> listOfLineEntries) {
        for (TimePointId timePointId : listOfTimePointIds) {
            String pointId = "INPUT_EVENT_ID=" + timePointId.getId();
            String time = "TIME=" + timePointId.getTime();

            listOfLineEntries.add(
                    new ArrayList(Arrays.asList(time, "IP.SRC=10.223.3.7", "IP.DST=10.223.3.8", pointId))
            );

        };
    }

    @Test
    public void testLinesRead() {
        Assert.assertEquals(retroReaderPointIds.getLinesRead(), lineCount);

    }

    @Test
    public void testOneTickToMultipleQuotes() {
        Assert.assertEquals(retroReaderPointIds.getListOfPointIds(), listOfIdsPointIds);
    }

}
