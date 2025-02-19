package org.com.openmarket.market.domain.core.usecase.offer.refuseOffer.exception;

import org.com.openmarket.market.domain.core.usecase.common.exception.core.ForbiddenException;

public class InvalidOfferPermissionException extends ForbiddenException {
    public InvalidOfferPermissionException() {
        super("User has no permission to handle this offer.");
    }
}
