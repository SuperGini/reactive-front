package com.gini.config;

import com.gini.exceptions.ReactiveCoreClientExceptions;
import com.gini.exceptions.ReactiveCoreServerException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

//https://medium.com/nerd-for-tech/webclient-error-handling-made-easy-4062dcf58c49   -> error handling
@Configuration
public class WebErrorHandlerConfig {

    @Bean
    public ExchangeFilterFunction errorHandler() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {

            if (clientResponse.statusCode().is5xxServerError()) {
                return clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new ReactiveCoreServerException(errorBody)));

            } else if (clientResponse.statusCode().is4xxClientError()) {
                return clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new ReactiveCoreClientExceptions(errorBody)));

            } else {
                return Mono.just(clientResponse);
            }
        });
    }
}
