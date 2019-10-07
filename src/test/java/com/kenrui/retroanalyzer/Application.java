package com.kenrui.retroanalyzer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenrui.retroanalyzer.database.repositories.RepeatingCorrelationGroupsRepository;
import com.kenrui.retroanalyzer.reader.RetroReaderCorrelationIds;
import com.kenrui.retroanalyzer.reader.RetroReaderPointIds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
        System.out.println("I'm here");
    }

}