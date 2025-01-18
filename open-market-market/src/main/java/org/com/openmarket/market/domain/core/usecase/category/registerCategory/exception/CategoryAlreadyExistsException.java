package org.com.openmarket.market.domain.core.usecase.category.registerCategory.exception;

import org.com.openmarket.market.domain.core.usecase.common.exception.core.ConflictException;

import java.text.MessageFormat;

public class CategoryAlreadyExistsException extends ConflictException {
    private final static String MESSAGE = "Category {0} already exists!";

    public CategoryAlreadyExistsException(String categoryName) {
        super(MessageFormat.format(MESSAGE, categoryName));
    }
}
