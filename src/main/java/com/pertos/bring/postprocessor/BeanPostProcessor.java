package com.pertos.bring.postprocessor;

public interface BeanPostProcessor {

    default Object postProcessBeforeInitialization(Class<?> beanClass, Object bean) {
        return bean;
    }

    default Object postProcessAfterInitialization(Class<?> beanClass, Object bean) {
        return bean;
    }
}
