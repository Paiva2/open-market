package org.com.openmarket.market.domain.core.usecase.common.exception;

public class ItemNotFoundException extends RuntimeException {
    private final static String MESSAGE_DEFAULT = "Item not found!";

    public ItemNotFoundException() {
        super(MESSAGE_DEFAULT);
    }
}
