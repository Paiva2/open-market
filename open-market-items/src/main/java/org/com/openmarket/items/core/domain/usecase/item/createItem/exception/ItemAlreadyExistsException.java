package org.com.openmarket.items.core.domain.usecase.item.createItem.exception;

import java.text.MessageFormat;

public class ItemAlreadyExistsException extends RuntimeException {
    private static final String MESSAGE = "Item with name {0} already exists!";

    public ItemAlreadyExistsException(String itemName) {
        super(MessageFormat.format(MESSAGE, itemName));
    }
}
