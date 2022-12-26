package com.gini;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@ConfigurationPropertiesScan("com.gini.config")
public class ReactiveFrontApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveFrontApplication.class, args);
    }

}
