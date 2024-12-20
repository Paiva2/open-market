package org.com.openmarket.wallet.application.gateway.controller.walletLedger.exception;

public class ExternalIdMissingException extends RuntimeException {
    private final static String MESSAGE = "Missing authentication token external id!";

    public ExternalIdMissingException() {
        super(MESSAGE);
    }
}
