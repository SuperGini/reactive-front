package com.gini.config;

import com.gini.gateway.ReactiveCoreClient;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

//https://www.baeldung.com/spring-5-webclient
//https://www.baeldung.com/spring-webflux-timeout
//https://www.youtube.com/watch?v=TR254zh-f3c
@Configuration
public class WebClientConfig {

    @Bean
    public HttpClient httpClient() {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .responseTimeout(Duration.ofSeconds(10))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(10, TimeUnit.SECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(10, TimeUnit.SECONDS)));
    }

    //build the web client
    @Bean
    public WebClient reactiveCoreWebClient(HttpClient httpClient) {

        return WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .baseUrl("http://localhost:8090")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    //build the factory that contains the web client
    @Bean
    public HttpServiceProxyFactory httpServiceProxyFactory(WebClient client) {
        return HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(client))
                .build();
    }

    //generate the client that calls the reactive-core microservice
    @Bean
    public ReactiveCoreClient reactiveCoreClient(HttpServiceProxyFactory factory) {
        return factory.createClient(ReactiveCoreClient.class);
    }
}
