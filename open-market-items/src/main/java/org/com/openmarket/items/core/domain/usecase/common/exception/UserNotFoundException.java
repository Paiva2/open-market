package org.com.openmarket.items.core.domain.usecase.common.exception;

import org.com.openmarket.items.core.domain.usecase.common.exception.core.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    private final static String MESSAGE = "User not found!";

    public UserNotFoundException() {
        super(MESSAGE);
    }
}
