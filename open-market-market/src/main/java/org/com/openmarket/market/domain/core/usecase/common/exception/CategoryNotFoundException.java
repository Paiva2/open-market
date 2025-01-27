package org.com.openmarket.market.domain.core.usecase.common.exception;

import org.com.openmarket.market.domain.core.usecase.common.exception.core.NotFoundException;

public class CategoryNotFoundException extends NotFoundException {
    private static final String MESSAGE = "Category not found.";

    public CategoryNotFoundException(String message) {
        super(message);
    }

    public CategoryNotFoundException() {
        super(MESSAGE);
    }
}
