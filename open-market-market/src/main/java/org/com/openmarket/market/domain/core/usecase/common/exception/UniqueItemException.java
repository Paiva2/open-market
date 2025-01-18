package org.com.openmarket.market.domain.core.usecase.common.exception;

import org.com.openmarket.market.domain.core.usecase.common.exception.core.BadRequestException;

public class UniqueItemException extends BadRequestException {
    private final static String MESSAGE_DEFAULT = "Item is unique!";

    public UniqueItemException() {
        super(MESSAGE_DEFAULT);
    }
}
