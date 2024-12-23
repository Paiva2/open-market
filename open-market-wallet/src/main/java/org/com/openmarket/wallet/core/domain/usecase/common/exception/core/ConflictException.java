package org.com.openmarket.wallet.core.domain.usecase.common.exception.core;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
