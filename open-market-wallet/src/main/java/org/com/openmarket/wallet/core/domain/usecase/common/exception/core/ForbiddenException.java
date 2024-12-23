package org.com.openmarket.wallet.core.domain.usecase.common.exception.core;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
