package org.com.openmarket.items.application.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionConfig {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> methodArgumentNotValidExceptionResolver(MethodArgumentNotValidException exception) {
        LinkedHashMap<String, Object> errors = mapErrors(
            HttpStatus.BAD_REQUEST.value(),
            exception.getMessage(),
            exception.getClass().getSimpleName()
        );

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    private LinkedHashMap<String, Object> mapErrors(Integer status, String message, String exceptionName) {
        return new LinkedHashMap<>() {{
            put("timestamp", new Date());
            put("status", status);
            put("message", message);
            put("exception", exceptionName);
        }};
    }
}
