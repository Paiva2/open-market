package org.com.openmarket.market.application.gateway.controller.common.exception;

public class ExternalIdMissingException extends RuntimeException {
    public ExternalIdMissingException() {
        super("Missing external user id.");
    }
}
