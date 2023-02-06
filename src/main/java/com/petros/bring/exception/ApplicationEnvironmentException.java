package com.petros.bring.exception;

/**
 * RuntimeException thrown in case of failing during initialization
 * of {@link com.petros.bring.environment.ApplicationEnvironment}
 */
public class ApplicationEnvironmentException extends RuntimeException {
    public ApplicationEnvironmentException(String message) {
        super(message);
    }
}
