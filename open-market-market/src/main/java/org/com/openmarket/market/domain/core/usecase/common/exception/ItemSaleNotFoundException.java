package org.com.openmarket.market.domain.core.usecase.common.exception;

import org.com.openmarket.market.domain.core.usecase.common.exception.core.NotFoundException;

public class ItemSaleNotFoundException extends NotFoundException {
    private final static String MESSAGE = "Item sale not found!";

    public ItemSaleNotFoundException() {
        super(MESSAGE);
    }
}
