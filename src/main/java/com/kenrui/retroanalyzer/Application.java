package com.kenrui.retroanalyzer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.kenrui.retroanalyzer.database.entities.Correlation;
import com.kenrui.retroanalyzer.reader.RetroReaderCorrelationIds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Scanner;

//http://zetcode.com/springboot/commandlinerunner/
@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired private RetroReaderCorrelationIds retroReaderCorrelationIds;

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        retroReaderCorrelationIds = new RetroReaderCorrelationIds(filename, ",", "Point 25", "TIME", "INPUT_EVENT_ID", "OUTPUT_EVENT_ID", correlationRepository);
//        retroReaderCorrelationIds.parseFile();
//        List<Correlation> correlationList = retroReaderCorrelationIds.getAll();
//        Long linesRead = retroReaderCorrelationIds.getLinesRead();
//        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);;
//        System.out.println(objectMapper.writeValueAsString(correlationList));
//        Scanner input = new Scanner(System.in);
//        String jibberish = input.next();
    }

}