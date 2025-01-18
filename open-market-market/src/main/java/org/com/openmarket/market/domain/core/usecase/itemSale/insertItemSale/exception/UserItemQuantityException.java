package org.com.openmarket.market.domain.core.usecase.itemSale.insertItemSale.exception;

import org.com.openmarket.market.domain.core.usecase.common.exception.core.BadRequestException;

import java.text.MessageFormat;

public class UserItemQuantityException extends BadRequestException {
    private final static String MESSAGE_DEFAULT = "User has no quantity of items available. Available: {0}.";

    public UserItemQuantityException(String quantity) {
        super(MessageFormat.format(MESSAGE_DEFAULT, quantity));
    }
}
