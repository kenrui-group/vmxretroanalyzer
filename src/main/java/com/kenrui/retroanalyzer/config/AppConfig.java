package com.kenrui.retroanalyzer.config;

import com.kenrui.retroanalyzer.database.config.JPAConfig;
import com.kenrui.retroanalyzer.database.repositories.CorrelationRepository;
import com.kenrui.retroanalyzer.database.repositories.PointRespository;
import com.kenrui.retroanalyzer.reader.RetroReaderCorrelationIds;
import com.kenrui.retroanalyzer.reader.RetroReaderPointIds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;

@Configuration
public class AppConfig {
//    Config defaultConfig = ConfigFactory.parseFile(new File("configs/defaults.conf"));

    @Autowired private CorrelationRepository correlationRepository;
    @Autowired private PointRespository pointRespository;

    private String filename = "C:\\Users\\Titus\\Desktop\\point25.txt";
    private String filenamePointId = "C:\\Users\\Titus\\Desktop\\point5wa.txt";

    @Bean
    public RetroReaderCorrelationIds retroReaderCorrelationIds() {
        return new RetroReaderCorrelationIds(filename, ",", "POINT25", "TIME", "INPUT_EVENT_ID", "OUTPUT_EVENT_ID", correlationRepository);
    }

    @Bean
    public RetroReaderPointIds retroReaderPointIds() {
        return new RetroReaderPointIds(filenamePointId, ",", "POINT5WA", "TIME", "ID", pointRespository);
    }
}
