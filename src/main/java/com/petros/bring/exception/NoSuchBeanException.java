package com.petros.bring.exception;

public class NoSuchBeanException extends RuntimeException {
    public NoSuchBeanException(String message) {
        super(message);
    }
}
