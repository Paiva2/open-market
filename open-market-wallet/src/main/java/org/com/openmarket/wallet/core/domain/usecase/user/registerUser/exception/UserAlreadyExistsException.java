package org.com.openmarket.wallet.core.domain.usecase.user.registerUser.exception;

import org.com.openmarket.wallet.core.domain.usecase.common.exception.core.ConflictException;

public class UserAlreadyExistsException extends ConflictException {
    private final static String MESSAGE = "User already exists!";

    public UserAlreadyExistsException() {
        super(MESSAGE);
    }
}
