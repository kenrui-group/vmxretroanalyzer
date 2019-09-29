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

    // SUT
    @Autowired private RetroReaderPointIds retroReaderPointIds;
    private int lineCount;

    private List<TimePointId> listOfIdsPointIds;

    private RetroReaderTestGenerator retroReaderTestGenerator;


    @BeforeClass
    public void setup() {

        retroReaderTestGenerator = new RetroReaderTestGenerator();
        retroReaderTestGenerator.generatePointIds();

        lineCount = retroReaderTestGenerator.getLineCountPointIds();
        listOfIdsPointIds = retroReaderTestGenerator.getListOfIdsPointIds();

        retroReaderPointIds.parseFile();
    }

    @Test
    public void testLinesRead() {
        Assert.assertEquals(retroReaderPointIds.getLinesRead(), lineCount);

    }

    @Test
    public void testGetListOfPointIds() {
        Assert.assertEquals(retroReaderPointIds.getListOfPointIds(), listOfIdsPointIds);
    }

}
