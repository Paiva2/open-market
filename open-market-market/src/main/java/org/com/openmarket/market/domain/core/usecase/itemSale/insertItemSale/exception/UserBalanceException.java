package org.com.openmarket.market.domain.core.usecase.itemSale.insertItemSale.exception;

import org.com.openmarket.market.domain.core.usecase.common.exception.core.BadRequestException;

public class UserBalanceException extends BadRequestException {
    public UserBalanceException(String message) {
        super(message);
    }
}
