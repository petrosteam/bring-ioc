package com.petros.bring.exception;

/**
 * Exception occurred during initialization of {@link com.petros.bring.Application} class.
 */
public class RunApplicationContextException extends RuntimeException {

    public RunApplicationContextException(String message) {
        super(message);
    }
}
