package com.petros.bring.postprocessor;

import com.petros.bring.bean.factory.BeanFactory;

public interface BeanPostProcessor {

    default Object postProcessBeforeInitialization(Class<?> beanClass, Object bean) {
        return bean;
    }

    default Object postProcessAfterInitialization(Class<?> beanClass, Object bean) {
        return bean;
    }

   // <T> void postProcessBeforeInitialization(Class<T> beanType, T obj, BeanFactory annotationBeanFactory);
}
