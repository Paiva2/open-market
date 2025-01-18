package org.com.openmarket.market.domain.core.usecase.common.exception;

import org.com.openmarket.market.domain.core.usecase.common.exception.core.NotFoundException;

public class UserItemNotFoundException extends NotFoundException {
    private final static String MESSAGE_DEFAULT = "User item not found!";

    public UserItemNotFoundException() {
        super(MESSAGE_DEFAULT);
    }
}
