package org.com.openmarket.items.application.gateway.controller.exception;

public class ExternalIdMissingException extends RuntimeException {
    private final static String MESSAGE = "External id not present!";

    public ExternalIdMissingException() {
        super(MESSAGE);
    }
}
