package org.com.openmarket.items.core.domain.usecase.item.uploadImage.exception;

import org.com.openmarket.items.core.domain.usecase.common.exception.core.BadRequestException;

public class InvalidExtensionException extends BadRequestException {
    public static String MESSAGE = "Invalid file format. Valid formats are: PNG, JPG, JPEG.";

    public InvalidExtensionException() {
        super(MESSAGE);
    }
}