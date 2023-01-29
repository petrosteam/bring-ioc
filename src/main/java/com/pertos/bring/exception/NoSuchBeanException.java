package com.pertos.bring.exception;

/**
 * Thrown when no one bean instance could be found by required type or name
 */
public class NoSuchBeanException extends RuntimeException {
    public NoSuchBeanException(String message) {
        super(message);
    }
}
