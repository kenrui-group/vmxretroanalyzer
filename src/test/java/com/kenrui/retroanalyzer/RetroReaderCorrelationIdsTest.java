package com.kenrui.retroanalyzer;

import com.kenrui.retroanalyzer.database.compositekeys.TimeCorrelationId;
import com.kenrui.retroanalyzer.database.entities.Correlation;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import com.kenrui.retroanalyzer.reader.RetroReaderCorrelationIds;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

//https://spring.io/blog/2016/04/15/testing-improvements-in-spring-boot-1-4
@SpringBootTest(classes = Application.class)
public class RetroReaderCorrelationIdsTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private RetroReaderCorrelationIds retroReaderCorrelationIds;

    private String filename = "C:\\Users\\Titus\\Desktop\\point25.txt";
    private BufferedReader bufferedReader;
    private List<List<String>> mockRetroFileContent;
    private int lineCount;

    private ArrayList<ArrayList<String>> listOfOneTickToMultipleQuotes;
    private ArrayList<ArrayList<String>> listOfOneTickToOneQuote;
    private ArrayList<ArrayList<String>> listOfDifferentTicksToSameQuote;
    private ArrayList<ArrayList<String>> listOfOneTickToNoQuote;
    private List<TimeCorrelationId> listOfIdsOneTickToMultipleQuotes;
    private List<TimeCorrelationId> listOfIdsOneTickToOneQuote;
    private List<TimeCorrelationId> listOfIdsDifferentTicksToSameQuote;
    private List<TimeCorrelationId> listOfIdsOneTickToNoQuote;


    // SUT
    private RetroReaderCorrelationIds retroReaderCorrelationIdsMock;

    @BeforeClass
    public void setup() {

        // Generate new retro file and check number of lines added
        try {
            FileWriter fileWriter = new FileWriter(filename);

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
        retroReaderCorrelationIdsMock = Mockito.spy(retroReaderCorrelationIds);
        retroReaderCorrelationIdsMock.parseFile();
    }

    private String getTime() {
        return getCurrentLocalDateTimeStamp();
    }

    public String getCurrentLocalDateTimeStamp() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }

    private void correlationIdToLineEntries(List<TimeCorrelationId> listOfTimeCorrelationIds, ArrayList<ArrayList<String>> listOfLineEntries) {
        for (TimeCorrelationId timeCorrelationId : listOfTimeCorrelationIds) {
            String tickId = "INPUT_EVENT_ID=" + timeCorrelationId.getId1();
            String quoteId = "OUTPUT_EVENT_ID=" + timeCorrelationId.getId2();
            String time = "TIME=" + timeCorrelationId.getTime();

            listOfLineEntries.add(
                    new ArrayList(Arrays.asList(time, "IP.SRC=10.223.3.7", "IP.DST=10.223.3.8", tickId, quoteId))
            );

        };
    }

    @Test
    public void testLinesRead() {
        Assert.assertEquals(retroReaderCorrelationIds.getLinesRead(), lineCount);

    }

    @Test
    public void testOneTickToMultipleQuotes() {
        Assert.assertEquals(retroReaderCorrelationIds.getListOfCorrelationIdsForOneTickToMultipleQuotes(), listOfIdsOneTickToMultipleQuotes);
    }

    @Test
    public void testOneTickToOneQuote() {
        Assert.assertEquals(retroReaderCorrelationIds.getListOfCorrelationIdsForOneTickToOneQuote(), listOfIdsOneTickToOneQuote);

    }

    @Test
    public void testDifferentTicksToSameQuote() {
        Assert.assertEquals(retroReaderCorrelationIds.getListOfCorrelationIdsForDifferentTicksToSameQuote(), listOfIdsDifferentTicksToSameQuote);
    }

    @Test
    public void testOneTickToNoQuote() {
        Assert.assertEquals(retroReaderCorrelationIds.getListOfCorrelationIdsForOneTickToNoQuote(), listOfIdsOneTickToNoQuote);
    }

}
