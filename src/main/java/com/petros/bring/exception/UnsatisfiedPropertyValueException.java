package com.petros.bring.exception;

/**
 * Exception thrown in cases of inappropriate attempt to read or write property ia a field of Class.
 */
public class UnsatisfiedPropertyValueException extends RuntimeException {

    private static final String MESSAGE_TEMPLATE = "Failed to set field: \"%s\".";

    public UnsatisfiedPropertyValueException(String field) {
        super(String.format(MESSAGE_TEMPLATE, field));
    }

}
