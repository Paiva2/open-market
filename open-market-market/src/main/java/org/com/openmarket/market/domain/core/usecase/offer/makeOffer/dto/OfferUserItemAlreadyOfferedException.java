package org.com.openmarket.market.domain.core.usecase.offer.makeOffer.dto;

import org.com.openmarket.market.domain.core.usecase.common.exception.core.ConflictException;

public class OfferUserItemAlreadyOfferedException extends ConflictException {
    public OfferUserItemAlreadyOfferedException(String message) {
        super(message);
    }
}
