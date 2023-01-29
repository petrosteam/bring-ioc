package com.pertos.bring.exception;

/**
 * Thrown when a bean doesn't match the expected type.
 */
public class BeanNotOfRequiredTypeException extends RuntimeException {
    public BeanNotOfRequiredTypeException(String message) {
        super(message);
    }
}
