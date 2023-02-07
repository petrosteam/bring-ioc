package com.petros.bring.exception;

import com.petros.bring.reader.BeanDefinition;

public class BeanCreationException extends RuntimeException {
    public BeanCreationException(String message) {
        super(message);
    }

    public BeanCreationException(BeanDefinition beanDefinition, Exception e) {
        super("Could not create bean %s because of %s".formatted(beanDefinition.getName(), e));
    }
}
