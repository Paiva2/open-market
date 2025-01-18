package org.com.openmarket.market.domain.core.usecase.user.registerUser.exception;

import org.com.openmarket.market.domain.core.usecase.common.exception.core.ConflictException;

import java.text.MessageFormat;

public class UserAlreadyExistsException extends ConflictException {
    private final static String MESSAGE = "User with external id {0} already exists!";

    public UserAlreadyExistsException(String externalId) {
        super(MessageFormat.format(MESSAGE, externalId));
    }
}
