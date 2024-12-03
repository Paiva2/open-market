package org.com.openmarket.items.core.domain.usecase.item.common.exception;

import org.com.openmarket.items.core.domain.usecase.common.exception.core.BadRequestException;

public class ItemNotActiveException extends BadRequestException {
    private final static String MESSAGE = "Item disabled!";

    public ItemNotActiveException() {
        super(MESSAGE);
    }
}
