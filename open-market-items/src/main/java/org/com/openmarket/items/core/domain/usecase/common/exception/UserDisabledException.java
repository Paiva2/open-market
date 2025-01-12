package org.com.openmarket.items.core.domain.usecase.common.exception;

import org.com.openmarket.items.core.domain.usecase.common.exception.core.ForbiddenException;

public class UserDisabledException extends ForbiddenException {
    private final static String MESSAGE = "User disabled.";

    public UserDisabledException() {
        super(MESSAGE);
    }
}
