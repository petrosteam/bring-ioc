package com.pertos.bring.postprocessor;

public class AutowiringBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Class<?> beanClass, Object bean) {
        return BeanPostProcessor.super.postProcessBeforeInitialization(beanClass, bean);
    }
}
