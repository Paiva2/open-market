package org.com.openmarket.market.domain.core.usecase.category.registerCategory.exception;

import java.text.MessageFormat;

public class CategoryAlreadyExistsException extends RuntimeException {
    private final static String MESSAGE = "Category {0} already exists!";

    public CategoryAlreadyExistsException(String categoryName) {
        super(MessageFormat.format(MESSAGE, categoryName));
    }
}
