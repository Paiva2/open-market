package org.com.openmarket.items.core.domain.usecase.item.common.exception;

import org.com.openmarket.items.core.domain.usecase.common.exception.core.NotFoundException;

public class ItemNotFoundException extends NotFoundException {
    private final static String MESSAGE = "Item not found!";

    public ItemNotFoundException() {
        super(MESSAGE);
    }
}
