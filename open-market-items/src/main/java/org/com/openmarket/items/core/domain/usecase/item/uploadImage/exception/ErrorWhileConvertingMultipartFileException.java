package org.com.openmarket.items.core.domain.usecase.item.uploadImage.exception;

import org.com.openmarket.items.core.domain.usecase.common.exception.core.BadRequestException;

public class ErrorWhileConvertingMultipartFileException extends BadRequestException {
    public ErrorWhileConvertingMultipartFileException(String message) {
        super(message);
    }
}
