package org.com.openmarket.market.domain.core.usecase.common.exception.core;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
