package org.com.openmarket.users.application.gateway.controller.exception;

public class ExternalIdMissingException extends RuntimeException {
    private final static String MESSAGE = "External id not present!";

    public ExternalIdMissingException() {
        super(MESSAGE);
    }
}
