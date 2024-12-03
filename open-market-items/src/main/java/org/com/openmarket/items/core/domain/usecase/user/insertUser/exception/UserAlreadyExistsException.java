package org.com.openmarket.items.core.domain.usecase.user.insertUser.exception;

import org.com.openmarket.items.core.domain.usecase.common.exception.core.ConflictException;

import java.text.MessageFormat;

public class UserAlreadyExistsException extends ConflictException {
    private static final String MESSAGE = "User with email: {0} already exists!";

    public UserAlreadyExistsException(String email) {
        super(MessageFormat.format(MESSAGE, email));
    }
}
