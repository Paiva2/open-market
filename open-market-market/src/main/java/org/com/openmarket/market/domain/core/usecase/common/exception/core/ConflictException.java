package org.com.openmarket.market.domain.core.usecase.common.exception.core;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
