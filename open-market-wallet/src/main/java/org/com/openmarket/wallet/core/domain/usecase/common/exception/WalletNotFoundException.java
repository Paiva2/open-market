package org.com.openmarket.wallet.core.domain.usecase.common.exception;

import org.com.openmarket.wallet.core.domain.usecase.common.exception.core.NotFoundException;

public class WalletNotFoundException extends NotFoundException {
    private final static String MESSAGE = "Wallet not found!";

    public WalletNotFoundException() {
        super(MESSAGE);
    }

    public WalletNotFoundException(String message) {
        super(message);
    }
}
