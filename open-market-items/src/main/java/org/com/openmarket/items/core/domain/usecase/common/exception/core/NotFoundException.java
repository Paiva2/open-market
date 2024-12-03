package org.com.openmarket.items.core.domain.usecase.common.exception.core;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
