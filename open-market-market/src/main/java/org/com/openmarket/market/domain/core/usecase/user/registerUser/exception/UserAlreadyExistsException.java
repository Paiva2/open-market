package org.com.openmarket.market.domain.core.usecase.user.registerUser.exception;

import java.text.MessageFormat;

public class UserAlreadyExistsException extends RuntimeException {
    private final static String MESSAGE = "User with external id {0} already exists!";

    public UserAlreadyExistsException(String externalId) {
        super(MessageFormat.format(MESSAGE, externalId));
    }
}
