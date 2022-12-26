package com.gini.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "reactive-core")
public class WebClientProperties {

    private String baseUrl;
    private int connectTimeout;
    private long readTimeout;
    private long writeTimeout;

}
