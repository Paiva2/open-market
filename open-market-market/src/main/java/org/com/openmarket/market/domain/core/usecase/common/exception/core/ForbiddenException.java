package org.com.openmarket.market.domain.core.usecase.common.exception.core;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
