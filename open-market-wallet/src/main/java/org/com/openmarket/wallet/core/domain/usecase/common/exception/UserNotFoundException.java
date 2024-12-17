package org.com.openmarket.wallet.core.domain.usecase.common.exception;

public class UserNotFoundException extends RuntimeException {
    private final static String MESSAGE = "User not found!";

    public UserNotFoundException() {
        super(MESSAGE);
    }
}
