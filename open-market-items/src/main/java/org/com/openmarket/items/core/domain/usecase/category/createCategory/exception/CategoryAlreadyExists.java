package org.com.openmarket.items.core.domain.usecase.category.createCategory.exception;

import org.com.openmarket.items.core.domain.usecase.common.exception.core.ConflictException;

import java.text.MessageFormat;

public class CategoryAlreadyExists extends ConflictException {
    private final static String MESSAGE = "Category with name {0} already exists!";

    public CategoryAlreadyExists(String name) {
        super(MessageFormat.format(MESSAGE, name));
    }
}
