package com.kenrui.retroanalyzer;

import com.kenrui.retroanalyzer.database.compositekeys.TimeCorrelationId;
import com.kenrui.retroanalyzer.database.compositekeys.TimePointId;
import com.kenrui.retroanalyzer.database.entities.Correlation;
import com.kenrui.retroanalyzer.database.entities.Point;
import com.kenrui.retroanalyzer.reader.RetroReaderCorrelationIds;
import com.kenrui.retroanalyzer.reader.RetroReaderPointIds;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

//https://spring.io/blog/2016/04/15/testing-improvements-in-spring-boot-1-4
@SpringBootTest(classes = Application.class)
public class RetroReaderCorrelationTest extends AbstractTestNGSpringContextTests {

    // SUTs
    @Autowired private RetroReaderCorrelationIds retroReaderCorrelationIds;
    @Autowired private RetroReaderPointIds retroReaderPointIds;

    private List<TimeCorrelationId> listOfIdsOneTickToMultipleQuotes;
    private List<TimeCorrelationId> listOfIdsOneTickToOneQuote;
    private List<TimeCorrelationId> listOfIdsDifferentTicksToSameQuote;
    private List<TimeCorrelationId> listOfIdsOneTickToNoQuote;


    private int lineCountCorrelationIds, lineCountPointIds;

    private List<TimePointId> listOfIdsPointIds;

    private RetroReaderTestGenerator retroReaderTestGenerator;

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
    public void testCorrelatePoints() {
        Map<Correlation, Point> correlatedPoints = retroReaderCorrelationIds.getListOfCorrelatedPoints();
    }

}
