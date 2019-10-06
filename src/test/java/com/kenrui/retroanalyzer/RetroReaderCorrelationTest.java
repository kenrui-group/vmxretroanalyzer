package com.kenrui.retroanalyzer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.kenrui.retroanalyzer.database.compositekeys.TimeCorrelationId;
import com.kenrui.retroanalyzer.database.compositekeys.TimePointId;
import com.kenrui.retroanalyzer.reader.RetroReaderCorrelationIds;
import com.kenrui.retroanalyzer.reader.RetroReaderPointIds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

//https://spring.io/blog/2016/04/15/testing-improvements-in-spring-boot-1-4
@SpringBootTest(classes = Application.class)
public class RetroReaderCorrelationTest extends AbstractTestNGSpringContextTests {

    // SUTs
    @Autowired
    private RetroReaderCorrelationIds retroReaderCorrelationIds;
    @Autowired
    private RetroReaderPointIds retroReaderPointIds;

    private List<TimeCorrelationId> listOfIdsOneTickToMultipleQuotes;
    private List<TimeCorrelationId> listOfIdsOneTickToOneQuote;
    private List<TimeCorrelationId> listOfIdsDifferentTicksToSameQuote;
    private List<TimeCorrelationId> listOfIdsOneTickToNoQuote;


    private int lineCountCorrelationIds, lineCountPointIds;

    private List<TimePointId> listOfIdsPointIds;

    private RetroReaderTestGenerator retroReaderTestGenerator;

    private ObjectMapper objectMapper;

    @BeforeClass
    public void setup() {
        retroReaderTestGenerator = new RetroReaderTestGenerator();
        retroReaderTestGenerator.generateCorrelationIds();

        lineCountCorrelationIds = retroReaderTestGenerator.getLineCountCorrelationIds();
        listOfIdsOneTickToMultipleQuotes = retroReaderTestGenerator.getListOfIdsOneTickToMultipleQuotes();
        listOfIdsOneTickToOneQuote = retroReaderTestGenerator.getListOfIdsOneTickToOneQuote();
        listOfIdsDifferentTicksToSameQuote = retroReaderTestGenerator.getListOfIdsDifferentTicksToSameQuote();
        listOfIdsOneTickToNoQuote = retroReaderTestGenerator.getListOfIdsOneTickToNoQuote();


        retroReaderCorrelationIds.parseFile();


        retroReaderTestGenerator = new RetroReaderTestGenerator();
        retroReaderTestGenerator.generatePointIds();

        lineCountPointIds = retroReaderTestGenerator.getLineCountPointIds();
        listOfIdsPointIds = retroReaderTestGenerator.getListOfIdsPointIds();


        retroReaderPointIds.parseFile();

        objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Test
    public void testLinesReadCorrelationIds() {
        Assert.assertEquals(retroReaderCorrelationIds.getLinesRead(), lineCountCorrelationIds);

    }

    @Test
    public void testLinesReadPointIds() {
        Assert.assertEquals(retroReaderPointIds.getLinesRead(), lineCountPointIds);

    }

    @Test
    public void testGetListOfPointIds() {
        Assert.assertEquals(retroReaderPointIds.getListOfPointIds(), listOfIdsPointIds);
    }

    @Test
    public void testFindCorrelationIdsNotInPoint() throws JsonProcessingException {
        List<TimeCorrelationId> timeCorrelationIds = retroReaderCorrelationIds.findCorrelationIdsNotInPoint();
        System.out.println("Found following " + timeCorrelationIds.size() + " correlation ids not correlated.");
        System.out.println(objectMapper.writeValueAsString(timeCorrelationIds));
    }

    @Test
    public void testCorrelate() throws JsonProcessingException {
        Map<TimeCorrelationId, TimePointId> correlatedPoints = retroReaderCorrelationIds.correlatePoints();
        System.out.println("Attempt to correlate returned " + correlatedPoints.size() + " points.");
        System.out.println(objectMapper.writeValueAsString(correlatedPoints));
    }

    @Test
    public void findCorrelations() {
        List<TimeCorrelationId> correlations = retroReaderCorrelationIds.findCorrelations();
    }

}
