package org.com.openmarket.wallet.core.domain.usecase.walletLedger.registerNewTransaction.exception;

import org.com.openmarket.wallet.core.domain.usecase.common.exception.core.BadRequestException;

public class InvalidValueException extends BadRequestException {
    public InvalidValueException(String message) {
        super(message);
    }
}
