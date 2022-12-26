package com.gini.error.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gini.exceptions.ReactiveCoreClientExceptions;
import com.gini.exceptions.ReactiveCoreServerException;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@AllArgsConstructor
public class GlobalErrorHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {


        if(ex instanceof ReactiveCoreClientExceptions){
            return mappingErrorResponse(exchange, ex.getMessage());
        }

        if(ex instanceof ReactiveCoreServerException){
            return mappingErrorResponse(exchange, ex.getMessage());
        }

        return mappingDefaultErrorResponse(exchange);
    }

    private Mono<Void> mappingErrorResponse(ServerWebExchange exchange, String errorBody) {

        DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
        DataBuffer errorResponse;

        try {
            ProblemDetail x = objectMapper.readValue(errorBody, ProblemDetail.class);

            exchange.getResponse().setStatusCode(HttpStatus.valueOf(x.getStatus()));
            exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_EVENT_STREAM);

            errorResponse = dataBufferFactory.wrap(errorBody.getBytes());

            return exchange.getResponse().writeWith(Mono.just(errorResponse));

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Mono<Void> mappingDefaultErrorResponse(ServerWebExchange exchange) {
        DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
        DataBuffer errorResponse;

        var httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        var error = ProblemDetail.forStatusAndDetail(httpStatus, "Unable to process request");
        error.setType(URI.create(""));

        exchange.getResponse().setStatusCode(httpStatus);
        exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_EVENT_STREAM);

        try {
            errorResponse = dataBufferFactory.wrap(objectMapper.writeValueAsBytes(error));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return exchange.getResponse().writeWith(Mono.just(errorResponse));
    }
}
