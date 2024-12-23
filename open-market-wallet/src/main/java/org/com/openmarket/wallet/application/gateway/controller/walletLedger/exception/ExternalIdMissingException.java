package org.com.openmarket.wallet.application.gateway.controller.walletLedger.exception;

import org.com.openmarket.wallet.core.domain.usecase.common.exception.core.ForbiddenException;

public class ExternalIdMissingException extends ForbiddenException {
    private final static String MESSAGE = "Missing authentication token external id!";

    public ExternalIdMissingException() {
        super(MESSAGE);
    }
}
