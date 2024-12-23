package org.com.openmarket.wallet.application.config;

import org.com.openmarket.wallet.core.domain.usecase.common.exception.core.BadRequestException;
import org.com.openmarket.wallet.core.domain.usecase.common.exception.core.ConflictException;
import org.com.openmarket.wallet.core.domain.usecase.common.exception.core.ForbiddenException;
import org.com.openmarket.wallet.core.domain.usecase.common.exception.core.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionsConfig {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        LinkedHashMap<String, Object> errors = mapErrors(
            HttpStatus.BAD_REQUEST.value(),
            e.getMessage(),
            e.getClass().getSimpleName()
        );

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        LinkedHashMap<String, Object> errors = mapErrors(
            HttpStatus.BAD_REQUEST.value(),
            e.getMessage(),
            e.getClass().getSimpleName()
        );

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(NotFoundException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        LinkedHashMap<String, Object> errors = mapErrors(
            status.value(),
            e.getMessage(),
            e.getClass().getSimpleName()
        );

        return new ResponseEntity<>(errors, status);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequestException(BadRequestException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        LinkedHashMap<String, Object> errors = mapErrors(
            status.value(),
            e.getMessage(),
            e.getClass().getSimpleName()
        );

        return new ResponseEntity<>(errors, status);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String, Object>> handleConflictException(ConflictException e) {
        HttpStatus status = HttpStatus.CONFLICT;

        LinkedHashMap<String, Object> errors = mapErrors(
            status.value(),
            e.getMessage(),
            e.getClass().getSimpleName()
        );

        return new ResponseEntity<>(errors, status);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Map<String, Object>> handleForbiddenException(ForbiddenException e) {
        HttpStatus status = HttpStatus.FORBIDDEN;

        LinkedHashMap<String, Object> errors = mapErrors(
            status.value(),
            e.getMessage(),
            e.getClass().getSimpleName()
        );

        return new ResponseEntity<>(errors, status);
    }

    private LinkedHashMap<String, Object> mapErrors(Integer status, String message, String exception) {
        return new LinkedHashMap<>() {{
            put("timestamp", new Date());
            put("status", status);
            put("message", message);
            put("exception", exception);
        }};
    }
}
