package com.petros.bring.exception;

public class NoUniqueBeanException extends RuntimeException {
    public NoUniqueBeanException(String message) {
        super(message);
    }
}
