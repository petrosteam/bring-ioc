package com.pertos.bring.exception;

/**
 * Thrown when more than one instance of required type has been found in the container
 */
public class NoUniqueBeanException extends RuntimeException {
    public NoUniqueBeanException(String message) {
        super(message);
    }
}
