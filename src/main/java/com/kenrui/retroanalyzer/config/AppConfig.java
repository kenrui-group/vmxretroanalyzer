package com.kenrui.retroanalyzer.config;

import com.kenrui.retroanalyzer.database.config.JPAConfig;
import com.kenrui.retroanalyzer.database.repositories.CorrelationRepository;
import com.kenrui.retroanalyzer.reader.RetroReaderCorrelationIds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;

@Configuration
public class AppConfig {
//    Config defaultConfig = ConfigFactory.parseFile(new File("configs/defaults.conf"));

    @Autowired
    private CorrelationRepository correlationRepository;

    private String filename = "C:\\Users\\Titus\\Desktop\\point25.txt";

    @Bean
    public RetroReaderCorrelationIds retroReaderCorrelationIds() {
        return new RetroReaderCorrelationIds(filename, ",", "POINT25", "TIME", "INPUT_EVENT_ID", "OUTPUT_EVENT_ID", correlationRepository);
    }


}
