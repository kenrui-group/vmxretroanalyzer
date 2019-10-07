package com.kenrui.retroanalyzer;


import com.kenrui.retroanalyzer.reader.RetroReaderCorrelationIds;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testng.Assert;


@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
public class SmokeTest {
    @Autowired
    private RetroReaderCorrelationIds retroReaderCorrelationIds;

    @Test
    public void contexLoads() throws Exception {
        Assert.assertNotNull(retroReaderCorrelationIds);
    }
}