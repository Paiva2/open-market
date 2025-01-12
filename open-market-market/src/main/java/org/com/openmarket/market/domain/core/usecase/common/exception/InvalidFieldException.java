package org.com.openmarket.market.domain.core.usecase.common.exception;

public class InvalidFieldException extends RuntimeException {
    public InvalidFieldException(String message) {
        super(message);
    }
}
