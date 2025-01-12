package org.com.openmarket.market.domain.core.usecase.common.exception;

public class UserItemNotFoundException extends RuntimeException {
    private final static String MESSAGE_DEFAULT = "User item not found!";

    public UserItemNotFoundException() {
        super(MESSAGE_DEFAULT);
    }
}
