package org.com.openmarket.items.core.domain.usecase.item.common.exception;

public class ItemNotFoundException extends RuntimeException {
    private final static String MESSAGE = "Item not found!";

    public ItemNotFoundException() {
        super(MESSAGE);
    }
}
