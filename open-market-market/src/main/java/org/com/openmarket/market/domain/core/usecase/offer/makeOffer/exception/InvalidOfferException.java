package org.com.openmarket.market.domain.core.usecase.offer.makeOffer.exception;

import org.com.openmarket.market.domain.core.usecase.common.exception.core.BadRequestException;

public class InvalidOfferException extends BadRequestException {
    public InvalidOfferException(String message) {
        super(message);
    }
}
