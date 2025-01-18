package org.com.openmarket.market.domain.core.usecase.common.exception;

import org.com.openmarket.market.domain.core.usecase.common.exception.core.NotFoundException;

public class ItemNotFoundException extends NotFoundException {
    private final static String MESSAGE_DEFAULT = "Item not found!";

    public ItemNotFoundException() {
        super(MESSAGE_DEFAULT);
    }
}
