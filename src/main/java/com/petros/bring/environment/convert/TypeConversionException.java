package com.petros.bring.environment.convert;

/**
 * Exception thrown during converting Class types
 */
public class TypeConversionException extends RuntimeException {

    private static final String ERROR_MESSAGE_TEMPLATE = "Failed to convert type: %s to: %s";

    public TypeConversionException(String source, String target) {
        super(String.format(ERROR_MESSAGE_TEMPLATE, source, target));
    }
}
