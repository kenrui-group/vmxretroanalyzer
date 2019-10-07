package com.kenrui.retroanalyzer.config;

import com.kenrui.retroanalyzer.RetroReaderTestGenerator;
import com.kenrui.retroanalyzer.database.repositories.CorrelationRepository;
import com.kenrui.retroanalyzer.database.repositories.PointRespository;
import com.kenrui.retroanalyzer.reader.RetroReaderCorrelationIds;
import com.kenrui.retroanalyzer.reader.RetroReaderPointIds;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.File;
import java.net.URL;

@SpringBootConfiguration
public class AppConfigTest {
    Config defaultConfig = ConfigFactory.parseResources("defaults.conf");

    @Autowired private CorrelationRepository correlationRepository;
    @Autowired private PointRespository pointRespository;

    @Bean
    public RetroReaderTestGenerator getRetroReaderTestGenerator() {
        String fileNameCorrelation = defaultConfig.getString("retroDecodedFiles.correlation.file");
        String fileNamePoint = defaultConfig.getString("retroDecodedFiles.point.file");
        String name = defaultConfig.getString("retroDecodedFiles.correlation.name");
        String delimiter = defaultConfig.getString("retroDecodedFiles.correlation.delimiter");
        String time = defaultConfig.getString("retroDecodedFiles.correlation.fieldNames.time");
        String id1 = defaultConfig.getString("retroDecodedFiles.correlation.fieldNames.id1");
        String id2 = defaultConfig.getString("retroDecodedFiles.correlation.fieldNames.id2");
        String id = defaultConfig.getString("retroDecodedFiles.point.fieldNames.id");
        return new RetroReaderTestGenerator(fileNameCorrelation, fileNamePoint, delimiter, id1, id2, time, id);
    }

    @Bean
    public RetroReaderCorrelationIds retroReaderCorrelationIds() {
        String fileName = defaultConfig.getString("retroDecodedFiles.correlation.file");
        String name = defaultConfig.getString("retroDecodedFiles.correlation.name");
        String delimiter = defaultConfig.getString("retroDecodedFiles.correlation.delimiter");
        String time = defaultConfig.getString("retroDecodedFiles.correlation.fieldNames.time");
        String id1 = defaultConfig.getString("retroDecodedFiles.correlation.fieldNames.id1");
        String id2 = defaultConfig.getString("retroDecodedFiles.correlation.fieldNames.id2");
        File file = getFileFromWorkingDirectory(fileName);
        return new RetroReaderCorrelationIds(file, delimiter, name, time, id1, id2, correlationRepository);
    }

    @Bean
    public RetroReaderPointIds retroReaderPointIds() {
        String fileName = defaultConfig.getString("retroDecodedFiles.point.file");
        String name = defaultConfig.getString("retroDecodedFiles.point.name");
        String delimiter = defaultConfig.getString("retroDecodedFiles.point.delimiter");
        String time = defaultConfig.getString("retroDecodedFiles.point.fieldNames.time");
        String id = defaultConfig.getString("retroDecodedFiles.point.fieldNames.id");
        File file = getFileFromWorkingDirectory(fileName);
        return new RetroReaderPointIds(file, delimiter, name, time, id, pointRespository);
    }

    private File getFileFromWorkingDirectory(String fileName) {
        String path = System.getProperty("user.dir");
        return new File(path + "//" + fileName);
    }

    private File getFileFromResources(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }

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
