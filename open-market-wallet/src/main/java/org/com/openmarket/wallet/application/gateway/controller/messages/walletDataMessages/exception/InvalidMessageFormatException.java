package org.com.openmarket.wallet.application.gateway.controller.messages.walletDataMessages.exception;

public class InvalidMessageFormatException extends RuntimeException {
    public InvalidMessageFormatException(String message) {
        super(message);
    }
}
