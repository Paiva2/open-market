package org.com.openmarket.items.core.domain.usecase.common.exception.core;

public class ForbiddenException extends RuntimeException {
    
    public ForbiddenException(String message) {
        super(message);
    }
}
