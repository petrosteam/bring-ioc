package com.petros.bring.exception;

public class MoreThanOneConstructorException extends RuntimeException{
    public MoreThanOneConstructorException(String message) {
        super(message);
    }
}
