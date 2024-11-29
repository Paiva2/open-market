package org.com.openmarket.items.core.domain.usecase.user.insertUser.exception;

import java.text.MessageFormat;

public class UserAlreadyExistsException extends RuntimeException {
    private static final String MESSAGE = "User with email: {0} already exists!";

    public UserAlreadyExistsException(String email) {
        super(MessageFormat.format(MESSAGE, email));
    }
}
