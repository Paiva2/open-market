package org.com.openmarket.items.application.config;

import org.com.openmarket.items.core.domain.usecase.common.exception.core.BadRequestException;
import org.com.openmarket.items.core.domain.usecase.common.exception.core.ConflictException;
import org.com.openmarket.items.core.domain.usecase.common.exception.core.NotFoundException;
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

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> notFoundExceptionResolver(NotFoundException exception) {
        LinkedHashMap<String, Object> errors = mapErrors(
            HttpStatus.NOT_FOUND.value(),
            exception.getMessage(),
            exception.getClass().getSimpleName()
        );

        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> badRequestExceptionResolver(BadRequestException exception) {
        LinkedHashMap<String, Object> errors = mapErrors(
            HttpStatus.BAD_REQUEST.value(),
            exception.getMessage(),
            exception.getClass().getSimpleName()
        );

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String, Object>> conflictExceptionResolver(ConflictException exception) {
        LinkedHashMap<String, Object> errors = mapErrors(
            HttpStatus.CONFLICT.value(),
            exception.getMessage(),
            exception.getClass().getSimpleName()
        );

        return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
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
