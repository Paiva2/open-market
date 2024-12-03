package org.com.openmarket.items.core.domain.usecase.item.common.exception;

public class ItemNotActiveException extends RuntimeException {
    private final static String MESSAGE = "Item disabled!";

    public ItemNotActiveException() {
        super(MESSAGE);
    }
}
