package org.com.openmarket.users.application.gateway.controller.exception;

import org.com.openmarket.users.core.domain.usecase.common.exception.NotFoundException;

public class ExternalIdMissingException extends NotFoundException {
    private final static String MESSAGE = "External id not present!";

    public ExternalIdMissingException() {
        super(MESSAGE);
    }
}
