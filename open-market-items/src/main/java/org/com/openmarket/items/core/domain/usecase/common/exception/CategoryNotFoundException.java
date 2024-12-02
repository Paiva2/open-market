package org.com.openmarket.items.core.domain.usecase.common.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException() {

    }

    public CategoryNotFoundException(String message) {
        super(message);
    }
}
