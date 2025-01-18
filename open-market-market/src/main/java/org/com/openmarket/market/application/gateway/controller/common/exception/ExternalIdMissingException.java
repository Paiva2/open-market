package org.com.openmarket.market.application.gateway.controller.common.exception;

import org.com.openmarket.market.domain.core.usecase.common.exception.core.ForbiddenException;

public class ExternalIdMissingException extends ForbiddenException {
    public ExternalIdMissingException() {
        super("Missing external user id.");
    }
}
