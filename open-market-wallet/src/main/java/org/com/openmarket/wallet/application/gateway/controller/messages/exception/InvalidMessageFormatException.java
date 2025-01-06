package org.com.openmarket.wallet.application.gateway.controller.messages.exception;

public class InvalidMessageFormatException extends RuntimeException {
    public InvalidMessageFormatException(String message) {
        super(message);
    }
}
