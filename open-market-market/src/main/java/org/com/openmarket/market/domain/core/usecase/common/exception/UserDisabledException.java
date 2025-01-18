package org.com.openmarket.market.domain.core.usecase.common.exception;

import org.com.openmarket.market.domain.core.usecase.common.exception.core.ForbiddenException;

public class UserDisabledException extends ForbiddenException {
    private final static String MESSAGE_DEFAULT = "User disabled!";

    public UserDisabledException() {
        super(MESSAGE_DEFAULT);
    }
}
