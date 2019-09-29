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

    // SUT
    @Autowired private RetroReaderCorrelationIds retroReaderCorrelationIds;
    private int lineCount;

    private List<TimeCorrelationId> listOfIdsOneTickToMultipleQuotes;
    private List<TimeCorrelationId> listOfIdsOneTickToOneQuote;
    private List<TimeCorrelationId> listOfIdsDifferentTicksToSameQuote;
    private List<TimeCorrelationId> listOfIdsOneTickToNoQuote;

    private RetroReaderTestGenerator retroReaderTestGenerator;

    @BeforeClass
    public void setup() {
        retroReaderTestGenerator = new RetroReaderTestGenerator();
        retroReaderTestGenerator.generateCorrelationIds();

        lineCount = retroReaderTestGenerator.getLineCountCorrelationIds();
        listOfIdsOneTickToMultipleQuotes = retroReaderTestGenerator.getListOfIdsOneTickToMultipleQuotes();
        listOfIdsOneTickToOneQuote = retroReaderTestGenerator.getListOfIdsOneTickToOneQuote();
        listOfIdsDifferentTicksToSameQuote = retroReaderTestGenerator.getListOfIdsDifferentTicksToSameQuote();
        listOfIdsOneTickToNoQuote = retroReaderTestGenerator.getListOfIdsOneTickToNoQuote();

        retroReaderCorrelationIds.parseFile();
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
