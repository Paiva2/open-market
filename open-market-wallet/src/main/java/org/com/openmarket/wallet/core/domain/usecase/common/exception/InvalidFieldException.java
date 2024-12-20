package org.com.openmarket.wallet.core.domain.usecase.common.exception;

import java.text.MessageFormat;

public class InvalidFieldException extends RuntimeException {
    private final static String MESSAGE = "Invalid field: {0}.";

    public InvalidFieldException(String fieldName) {
        super(MessageFormat.format(MESSAGE, fieldName));
    }
}
