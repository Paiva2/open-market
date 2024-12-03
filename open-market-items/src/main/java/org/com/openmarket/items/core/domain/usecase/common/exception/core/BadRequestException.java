package org.com.openmarket.items.core.domain.usecase.common.exception.core;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
