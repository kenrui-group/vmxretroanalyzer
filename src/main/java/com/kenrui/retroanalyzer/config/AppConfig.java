package com.kenrui.retroanalyzer.config;

import com.kenrui.retroanalyzer.database.config.JPAConfig;
import com.kenrui.retroanalyzer.database.repositories.CorrelationRepository;
import com.kenrui.retroanalyzer.database.repositories.PointRespository;
import com.kenrui.retroanalyzer.reader.RetroReaderCorrelationIds;
import com.kenrui.retroanalyzer.reader.RetroReaderPointIds;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.hibernate.boot.model.naming.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;

import java.io.File;

@Configuration
public class AppConfig {
    Config defaultConfig = ConfigFactory.parseFile(new File("configs/defaults.conf"));

    @Autowired private CorrelationRepository correlationRepository;
    @Autowired private PointRespository pointRespository;

    @Bean
    public RetroReaderCorrelationIds retroReaderCorrelationIds() {
        String fileName = defaultConfig.getString("retroDecodedFiles.correlation.file");
        String name = defaultConfig.getString("retroDecodedFiles.correlation.name");
        String delimiter = defaultConfig.getString("retroDecodedFiles.correlation.delimiter");
        String time = defaultConfig.getString("retroDecodedFiles.correlation.fieldNames.time");
        String id1 = defaultConfig.getString("retroDecodedFiles.correlation.fieldNames.id1");
        String id2 = defaultConfig.getString("retroDecodedFiles.correlation.fieldNames.id2");
        return new RetroReaderCorrelationIds(fileName, delimiter, name, time, id1, id2, correlationRepository);
    }

    @Bean
    public RetroReaderPointIds retroReaderPointIds() {
        String fileName = defaultConfig.getString("retroDecodedFiles.point.file");
        String name = defaultConfig.getString("retroDecodedFiles.point.name");
        String delimiter = defaultConfig.getString("retroDecodedFiles.point.delimiter");
        String time = defaultConfig.getString("retroDecodedFiles.point.fieldNames.time");
        String id = defaultConfig.getString("retroDecodedFiles.point.fieldNames.id");
        return new RetroReaderPointIds(fileName, delimiter, name, time, id, pointRespository);
    }

//    https://stackoverflow.com/questions/43270058/sql-error-1054-sqlstate-42s22-unknown-column-in-field-list-error-java-sprin
//    @Bean
//    public PhysicalNamingStrategy physical() {
//        return new PhysicalNamingStrategyStandardImpl();
//    }
//
//    @Bean
//    public ImplicitNamingStrategy implicit() {
//        return new ImplicitNamingStrategyJpaCompliantImpl();
//    }
}
