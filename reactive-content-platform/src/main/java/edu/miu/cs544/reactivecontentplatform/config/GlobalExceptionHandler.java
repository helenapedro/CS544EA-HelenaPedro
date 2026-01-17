package edu.miu.cs544.reactivecontentplatform.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<String>> handleIllegalArg(IllegalArgumentException ex) {
        return Mono.just(ResponseEntity.badRequest().body(ex.getMessage()));
    }
}
