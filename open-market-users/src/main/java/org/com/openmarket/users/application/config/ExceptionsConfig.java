package org.com.openmarket.users.application.config;

import org.com.openmarket.users.core.domain.usecase.common.exception.NotFoundException;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
@Configuration
public class ExceptionsConfig {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, Object> errors = mapErrors(
            HttpStatus.BAD_REQUEST.value(),
            e.getMessage(),
            e.getClass().getSimpleName()
        );

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(NotFoundException e) {
        Map<String, Object> errors = mapErrors(
            HttpStatus.NOT_FOUND.value(),
            e.getMessage(),
            e.getClass().getSimpleName()
        );

        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    private Map<String, Object> mapErrors(Integer status, String message, String exceptionName) {
        return new LinkedHashMap<>() {{
            put("timestamp", new Date());
            put("status", status);
            put("message", message);
            put("exception", exceptionName);
        }};
    }
}
