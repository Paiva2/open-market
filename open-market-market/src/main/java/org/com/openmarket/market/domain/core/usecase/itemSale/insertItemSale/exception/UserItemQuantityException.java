package org.com.openmarket.market.domain.core.usecase.itemSale.insertItemSale.exception;

import java.text.MessageFormat;

public class UserItemQuantityException extends RuntimeException {
    private final static String MESSAGE_DEFAULT = "User has no quantity of items available. Available: {0}.";

    public UserItemQuantityException(String quantity) {
        super(MessageFormat.format(MESSAGE_DEFAULT, quantity));
    }
}
