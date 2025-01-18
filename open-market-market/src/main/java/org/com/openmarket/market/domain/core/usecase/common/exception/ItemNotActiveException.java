package org.com.openmarket.market.domain.core.usecase.common.exception;

import org.com.openmarket.market.domain.core.usecase.common.exception.core.BadRequestException;

public class ItemNotActiveException extends BadRequestException {
    private final static String MESSAGE_DEFAULT = "Item disabled!";

    public ItemNotActiveException() {
        super(MESSAGE_DEFAULT);
    }
}
