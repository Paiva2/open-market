package org.com.openmarket.market.domain.core.usecase.common.exception;

import org.com.openmarket.market.domain.core.usecase.common.exception.core.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    private final static String MESSAGE = "User not found!";

    public UserNotFoundException() {
        super(MESSAGE);
    }
}
