package org.com.openmarket.market.domain.core.usecase.common.exception;

import org.com.openmarket.market.domain.core.usecase.common.exception.core.BadRequestException;

public class InvalidFieldException extends BadRequestException {
    public InvalidFieldException(String message) {
        super(message);
    }
}
