package org.com.openmarket.items.core.domain.usecase.common.exception;

import org.com.openmarket.items.core.domain.usecase.common.exception.core.NotFoundException;

public class CategoryNotFoundException extends NotFoundException {
    private final static String MESSAGE = "Category not found!";

    public CategoryNotFoundException(String message) {
        super(message);
    }

    public CategoryNotFoundException() {
        super(MESSAGE);
    }
}
