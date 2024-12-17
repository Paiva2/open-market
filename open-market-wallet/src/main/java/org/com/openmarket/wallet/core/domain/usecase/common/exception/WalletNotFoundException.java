package org.com.openmarket.wallet.core.domain.usecase.common.exception;

public class WalletNotFoundException extends RuntimeException {
    private final static String MESSAGE = "Wallet not found!";

    public WalletNotFoundException() {
        super(MESSAGE);
    }

    public WalletNotFoundException(String message) {
        super(message);
    }
}
