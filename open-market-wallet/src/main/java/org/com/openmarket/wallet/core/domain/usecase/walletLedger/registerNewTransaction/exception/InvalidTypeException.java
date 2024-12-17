package org.com.openmarket.wallet.core.domain.usecase.walletLedger.registerNewTransaction.exception;

public class InvalidTypeException extends RuntimeException {
    private final static String MESSAGE = "Invalid transaction type! Type: ";

    public InvalidTypeException(String type) {
        super(MESSAGE + type);
    }
}
