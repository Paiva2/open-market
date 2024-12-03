package org.com.openmarket.items.core.domain.usecase.common.exception;

import org.com.openmarket.items.core.domain.usecase.common.exception.core.NotFoundException;

public class CategoryNotFoundException extends NotFoundException {
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
