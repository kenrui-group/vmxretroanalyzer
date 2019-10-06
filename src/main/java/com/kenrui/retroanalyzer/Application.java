package com.kenrui.retroanalyzer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.kenrui.retroanalyzer.database.compositekeys.TimeCorrelationId;
import com.kenrui.retroanalyzer.database.compositekeys.TimePointId;
import com.kenrui.retroanalyzer.database.entities.Correlation;
import com.kenrui.retroanalyzer.reader.RetroReaderCorrelationIds;
import com.kenrui.retroanalyzer.reader.RetroReaderPointIds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

//http://zetcode.com/springboot/commandlinerunner/
@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private RetroReaderCorrelationIds retroReaderCorrelationIds;
    @Autowired
    private RetroReaderPointIds retroReaderPointIds;

    private ObjectMapper objectMapper;

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        retroReaderCorrelationIds.parseFile();
        retroReaderPointIds.parseFile();
        objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        Long linesRead = retroReaderCorrelationIds.getLinesRead();
        System.out.println("Read " + linesRead + " lines from correlation retro.");

        linesRead = retroReaderPointIds.getLinesRead();
        System.out.println("Read " + linesRead + " lines from point retro.");

        List<TimeCorrelationId> timeCorrelationIds = retroReaderCorrelationIds.findCorrelationIdsNotInPoint();
        System.out.println("Found following " + timeCorrelationIds.size() + " correlation ids not correlated.");
        System.out.println(objectMapper.writeValueAsString(timeCorrelationIds));

        Map<TimeCorrelationId, TimePointId> correlatePoints = retroReaderCorrelationIds.correlatePoints();
        System.out.println("Attempt to correlate returned " + correlatePoints.size() + " points (may include correlation ids that doesn't have points to correlate with.");

        List<TimeCorrelationId> oneTickToMultipleQuotes = retroReaderCorrelationIds.getListOfCorrelationIdsForOneTickToMultipleQuotes();
        System.out.println("Found " + oneTickToMultipleQuotes.size() + " one tick to multiple quotes.");

        List<TimeCorrelationId> oneTickToOneQuote = retroReaderCorrelationIds.getListOfCorrelationIdsForOneTickToOneQuote();
        System.out.println("Found " + oneTickToOneQuote.size() + " one tick to one quote.");

        List<TimeCorrelationId> differentTicksToSameQuote = retroReaderCorrelationIds.getListOfCorrelationIdsForDifferentTicksToSameQuote();
        System.out.println("Found " + differentTicksToSameQuote.size() + " different ticks to same quote, or repeated correlation ids.");

        List<TimeCorrelationId> oneTickToNoQuote = retroReaderCorrelationIds.getListOfCorrelationIdsForOneTickToNoQuote();
        System.out.println("Found " + oneTickToNoQuote.size() + " one tick to no quote.");

        List<TimeCorrelationId> correlatedPoints = retroReaderCorrelationIds.correlatedPoints();
        System.out.println("Found " + correlatedPoints.size() + " points correlated.");

//        Scanner input = new Scanner(System.in);
//        String jibberish = input.next();
    }

}