package com.kenrui.retroanalyzer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.kenrui.retroanalyzer.database.compositekeys.TimeCorrelationId;
import com.kenrui.retroanalyzer.database.compositekeys.TimePointId;
import com.kenrui.retroanalyzer.database.entities.Correlation;
import com.kenrui.retroanalyzer.database.entities.Point;
import com.kenrui.retroanalyzer.database.entities.RepeatingCorrelationIdGroups;
import com.kenrui.retroanalyzer.database.repositories.RepeatingCorrelationGroupsRepository;
import com.kenrui.retroanalyzer.reader.RetroReaderCorrelationIds;
import com.kenrui.retroanalyzer.reader.RetroReaderPointIds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

//http://zetcode.com/springboot/commandlinerunner/
@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired private RetroReaderCorrelationIds retroReaderCorrelationIds;
    @Autowired private RetroReaderPointIds retroReaderPointIds;
    @Autowired private RepeatingCorrelationGroupsRepository repeatingCorrelationGroupsRepository;

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

        Integer repeatingLines = retroReaderCorrelationIds.findNumberOfRepeatingCorrelationIdGroups();
        System.out.println("Found " + repeatingLines + " lines of repeating Correlation Id Groups (id1 and id2)");

        List<RepeatingCorrelationIdGroups> repeatingCorrelationIdGroups = retroReaderCorrelationIds.findRepeatingCorrelationIdGroups();
        System.out.println("Of all the repeating lines, it can be grouped into " + repeatingCorrelationIdGroups.size() + " Correlation Id Groups (id1 and id2) after deduplication");
        System.out.println("The repeating Correlation Id Group are:");
        System.out.println(objectMapper.writeValueAsString(repeatingCorrelationIdGroups));

        List<TimeCorrelationId> timeCorrelationIds = retroReaderCorrelationIds.findCorrelationIdsNotInPoint();
        System.out.println("Found following " + timeCorrelationIds.size() + " correlation ids not correlated.");
        System.out.println(objectMapper.writeValueAsString(timeCorrelationIds));

        System.out.println();
        System.out.println("---------------------------------------------------------------------------------");
        System.out.println("Number of Correlation Ids Found when Excluding repeating Correlation Id Groups (id1 and id2)");
        System.out.println("---------------------------------------------------------------------------------");
        List<TimeCorrelationId> oneTickToMultipleQuotes = retroReaderCorrelationIds.getListOfCorrelationIdsForOneTickToMultipleQuotes();
        Integer oneTickToMultipleQuotesCorrelatable = retroReaderCorrelationIds.checkIfAllOneTickToMultipleQuotesAreCorrelatable();

        System.out.println("One Tick to Multiple Quotes:  " + oneTickToMultipleQuotes.size());
        if (oneTickToMultipleQuotes.size() == oneTickToMultipleQuotesCorrelatable.intValue()) {
            System.out.println("\t All correlated: Y");
        } else {
            System.out.println("\t Only correlated: " + oneTickToMultipleQuotesCorrelatable.intValue());
        }


        List<TimeCorrelationId> oneTickToOneQuote = retroReaderCorrelationIds.getListOfCorrelationIdsForOneTickToOneQuote();
        Integer oneTickToOneQuoteCorrelatable = retroReaderCorrelationIds.checkIfAllOneTickToOneQuoteAreCorrelatable();

        System.out.println("One Tick to One Quote:  " + oneTickToOneQuote.size());
        if (oneTickToOneQuote.size() == oneTickToOneQuoteCorrelatable.intValue()) {
            System.out.println("\t All correlated: Y");
        } else {
            System.out.println("\t Only correlated: " + oneTickToOneQuoteCorrelatable.intValue());
        }


        List<TimeCorrelationId> differentTicksToSameQuote = retroReaderCorrelationIds.getListOfCorrelationIdsForDifferentTicksToSameQuote();
        System.out.println("Different Tick to Same Quote:  " + differentTicksToSameQuote.size());

        List<TimeCorrelationId> oneTickToNoQuote = retroReaderCorrelationIds.getListOfCorrelationIdsForOneTickToNoQuote();
        System.out.println("One Tick to No Quote:  " + oneTickToNoQuote.size());


        System.out.println();
        System.out.println("---------------------------------------------------------------------------------");
        System.out.println("Number of Correlation Ids Found when Including repeating Correlation Id Groups (id1 and id2)");
        System.out.println("---------------------------------------------------------------------------------");
        Integer numberOfPointsFoundFromRepeatingCorrelationIdGroups = retroReaderCorrelationIds.findNumberOfPointsFoundFromRepeatingCorrelationIdGroups();
        System.out.println("One Tick to Multiple Quotes:  " + numberOfPointsFoundFromRepeatingCorrelationIdGroups );
        System.out.println("\t All correlated: Y");

        System.out.println();
        System.out.println("---------------------------------------------------------------------------------");
        System.out.println("Total correlated points");
        System.out.println("---------------------------------------------------------------------------------");
        Integer totalLines = oneTickToMultipleQuotes.size() + oneTickToOneQuote.size() + numberOfPointsFoundFromRepeatingCorrelationIdGroups;
        System.out.println("One Tick to Multiple Quotes + One Tick to One Quote + One Tick to One Quote (Deduplicated Correlation Id Groups) = " + totalLines + " points.");

//        List<TimeCorrelationId> correlatedPoints = retroReaderCorrelationIds.correlatedPoints();
//        System.out.println("Found " + correlatedPoints.size() + " points correlated.");

//        Scanner input = new Scanner(System.in);
//        String jibberish = input.next();
    }

}