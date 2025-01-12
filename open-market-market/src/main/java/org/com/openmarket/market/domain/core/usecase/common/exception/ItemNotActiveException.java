package org.com.openmarket.market.domain.core.usecase.common.exception;

public class ItemNotActiveException extends RuntimeException {
    private final static String MESSAGE_DEFAULT = "Item disabled!";

    public ItemNotActiveException() {
        super(MESSAGE_DEFAULT);
    }
}
