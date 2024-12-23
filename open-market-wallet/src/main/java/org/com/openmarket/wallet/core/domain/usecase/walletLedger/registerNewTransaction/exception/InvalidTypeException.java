package org.com.openmarket.wallet.core.domain.usecase.walletLedger.registerNewTransaction.exception;

import org.com.openmarket.wallet.core.domain.usecase.common.exception.core.BadRequestException;

public class InvalidTypeException extends BadRequestException {
    private final static String MESSAGE = "Invalid transaction type! Type: ";

    public InvalidTypeException(String type) {
        super(MESSAGE + type);
    }
}
