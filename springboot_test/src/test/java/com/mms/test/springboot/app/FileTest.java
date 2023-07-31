package com.mms.test.springboot.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Properties;

@SpringBootTest
@TestPropertySource(properties = { "spring.config.location = classpath:application-test.yml" })
public class FileTest {

//    @Value("${name.app}")
//    private String value;
//
//    @Test
//    public void imprimirSystemProperties() {
//        Properties properties = System.getProperties();
//        properties.forEach((key, value) -> System.out.println(key + " : " + value));
//
//        System.out.println("Value: " + value);
//    }
}
