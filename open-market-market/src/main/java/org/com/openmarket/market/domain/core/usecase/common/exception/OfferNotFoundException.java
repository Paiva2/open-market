package org.com.openmarket.market.domain.core.usecase.common.exception;

import org.com.openmarket.market.domain.core.usecase.common.exception.core.NotFoundException;

public class OfferNotFoundException extends NotFoundException {
    private static final String MESSAGE = "Offer not found!";

    public OfferNotFoundException() {
        super(MESSAGE);
    }
}
