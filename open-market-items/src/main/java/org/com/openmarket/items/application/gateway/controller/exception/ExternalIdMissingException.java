package org.com.openmarket.items.application.gateway.controller.exception;

import org.com.openmarket.items.core.domain.usecase.common.exception.core.BadRequestException;

public class ExternalIdMissingException extends BadRequestException {
    private final static String MESSAGE = "External id not present!";

    public ExternalIdMissingException() {
        super(MESSAGE);
    }
}
