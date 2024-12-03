package org.com.openmarket.items.core.domain.usecase.common.exception;

import org.com.openmarket.items.core.domain.usecase.common.exception.core.BadRequestException;

import java.text.MessageFormat;

public class InvalidFieldException extends BadRequestException {
    private static final String MESSAGE = "Invalid field! {0}.";

    public InvalidFieldException(String fieldName) {
        super(MessageFormat.format(MESSAGE, fieldName));
    }
}
