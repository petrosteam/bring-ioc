package com.pertos.bring.exception;

/**
 * Thrown when bean definition could not be created.
 */
public class BeanDefinitionStoreException extends RuntimeException {
    public BeanDefinitionStoreException(String message) {
        super(message);
    }
}
