package org.com.openmarket.market.application.config;

import org.com.openmarket.market.domain.core.usecase.common.exception.core.BadRequestException;
import org.com.openmarket.market.domain.core.usecase.common.exception.core.ConflictException;
import org.com.openmarket.market.domain.core.usecase.common.exception.core.ForbiddenException;
import org.com.openmarket.market.domain.core.usecase.common.exception.core.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionConfig {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, Object> errors = mapErrors(
            exception.getMessage(),
            exception.getClass().getSimpleName(),
            HttpStatus.BAD_REQUEST.value()
        );

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        Map<String, Object> errors = mapErrors(
            exception.getMessage(),
            exception.getClass().getSimpleName(),
            HttpStatus.BAD_REQUEST.value()
        );

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String, Object>> handleConflictException(ConflictException exception) {
        Map<String, Object> errors = mapErrors(
            exception.getMessage(),
            exception.getClass().getSimpleName(),
            HttpStatus.CONFLICT.value()
        );

        return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(NotFoundException exception) {
        Map<String, Object> errors = mapErrors(
            exception.getMessage(),
            exception.getClass().getSimpleName(),
            HttpStatus.NOT_FOUND.value()
        );

        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequestException(BadRequestException exception) {
        Map<String, Object> errors = mapErrors(
            exception.getMessage(),
            exception.getClass().getSimpleName(),
            HttpStatus.BAD_REQUEST.value()
        );

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Map<String, Object>> handleForbiddenException(ForbiddenException exception) {
        Map<String, Object> errors = mapErrors(
            exception.getMessage(),
            exception.getClass().getSimpleName(),
            HttpStatus.FORBIDDEN.value()
        );

        return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
    }

    private Map<String, Object> mapErrors(String message, String exceptionName, int status) {
        return new LinkedHashMap<>() {{
            put("timestamp", new Date());
            put("message", message);
            put("exception", exceptionName);
            put("status", status);
        }};
    }
}
