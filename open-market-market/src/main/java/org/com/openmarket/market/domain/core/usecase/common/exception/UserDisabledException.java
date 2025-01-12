package org.com.openmarket.market.domain.core.usecase.common.exception;

public class UserDisabledException extends RuntimeException {
    private final static String MESSAGE_DEFAULT = "User disabled!";

    public UserDisabledException() {
        super(MESSAGE_DEFAULT);
    }
}
