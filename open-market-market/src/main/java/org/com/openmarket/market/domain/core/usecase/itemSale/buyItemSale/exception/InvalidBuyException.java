package org.com.openmarket.market.domain.core.usecase.itemSale.buyItemSale.exception;

import org.com.openmarket.market.domain.core.usecase.common.exception.core.BadRequestException;

public class InvalidBuyException extends BadRequestException {
    public InvalidBuyException(String message) {
        super(message);
    }
}
