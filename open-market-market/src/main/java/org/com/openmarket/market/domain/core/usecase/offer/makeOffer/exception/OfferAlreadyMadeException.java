package org.com.openmarket.market.domain.core.usecase.offer.makeOffer.exception;

import org.com.openmarket.market.domain.core.usecase.common.exception.core.ConflictException;

public class OfferAlreadyMadeException extends ConflictException {
    public OfferAlreadyMadeException(String message) {
        super(message);
    }
}
