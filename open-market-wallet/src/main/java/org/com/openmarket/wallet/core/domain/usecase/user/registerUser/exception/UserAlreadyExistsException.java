package org.com.openmarket.wallet.core.domain.usecase.user.registerUser.exception;

public class UserAlreadyExistsException extends RuntimeException {
    private final static String MESSAGE = "User already exists!";

    public UserAlreadyExistsException() {
        super(MESSAGE);
    }
}
