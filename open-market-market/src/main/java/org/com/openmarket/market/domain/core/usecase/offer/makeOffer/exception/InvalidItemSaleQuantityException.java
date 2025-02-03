package org.com.openmarket.market.domain.core.usecase.offer.makeOffer.exception;

import org.com.openmarket.market.domain.core.usecase.common.exception.core.BadRequestException;

public class InvalidItemSaleQuantityException extends BadRequestException {
    public InvalidItemSaleQuantityException(String message) {
        super(message);
    }
}
