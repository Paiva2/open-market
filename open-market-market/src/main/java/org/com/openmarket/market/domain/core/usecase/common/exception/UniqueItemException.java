package org.com.openmarket.market.domain.core.usecase.common.exception;

public class UniqueItemException extends RuntimeException {
    private final static String MESSAGE_DEFAULT = "Item is unique!";

    public UniqueItemException() {
        super(MESSAGE_DEFAULT);
    }
}
