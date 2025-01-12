package org.com.openmarket.items.core.domain.usecase.common.exception;

import org.com.openmarket.items.core.domain.usecase.common.exception.core.NotFoundException;

public class UserItemNotFoundException extends NotFoundException {
    private final static String MESSAGE = "User item not found.";

    public UserItemNotFoundException() {
        super(MESSAGE);
    }
}
