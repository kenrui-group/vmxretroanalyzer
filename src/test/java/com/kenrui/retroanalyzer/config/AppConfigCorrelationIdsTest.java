package com.kenrui.retroanalyzer.config;

import com.kenrui.retroanalyzer.reader.RetroReaderCorrelationIds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import com.kenrui.retroanalyzer.database.repositories.CorrelationRepository;
import org.springframework.test.context.ContextConfiguration;

@Configuration
//@ContextConfiguration(classes=CorrelationRepository.class)
public class AppConfigCorrelationIdsTest {
//    Config defaultConfig = ConfigFactory.parseFile(new File("configs/defaults.conf"));

    @Autowired
    private CorrelationRepository correlationRepository;

    private String filename = "C:\\Users\\Titus\\Desktop\\point25.txt";

    @Bean
    public RetroReaderCorrelationIds retroReaderCorrelationIds() {
        return new RetroReaderCorrelationIds(filename, ",", "Point 25", "TIME", "INPUT_EVENT_ID", "OUTPUT_EVENT_ID", correlationRepository);
    }


}
