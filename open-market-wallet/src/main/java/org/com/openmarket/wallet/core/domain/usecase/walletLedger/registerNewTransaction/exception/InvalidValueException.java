package org.com.openmarket.wallet.core.domain.usecase.walletLedger.registerNewTransaction.exception;

public class InvalidValueException extends RuntimeException {
    public InvalidValueException(String message) {
        super(message);
    }
}
