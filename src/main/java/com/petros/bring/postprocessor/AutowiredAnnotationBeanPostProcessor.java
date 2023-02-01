package com.petros.bring.postprocessor;


import com.petros.bring.annotations.Autowired;
import com.petros.bring.annotations.Component;
import com.petros.bring.bean.factory.AnnotationBeanFactory;
import com.petros.bring.bean.factory.BeanFactory;

import java.util.Arrays;

@Component
public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {
    private final AnnotationBeanFactory factory;

    public AutowiredAnnotationBeanPostProcessor(AnnotationBeanFactory beanFactory) {
        this.factory = beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Class<?> beanClass, Object bean) {
        var annotatedFields = Arrays.stream(bean.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Autowired.class))
                .toList();

        for (var field : annotatedFields) {
            var value = field.getAnnotation(Autowired.class).value();
            var fieldType = field.getType();
            var beanToInject = this.getBean(value, fieldType);
            field.setAccessible(true);
            try {
                field.set(bean, beanToInject);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Can not inject " + e);
            }
        }
        return bean;
    }

    private Object getBean(String beanName, Class<?> clazz) {
        if (beanName.isEmpty()) {
            return factory.getBean(clazz);
        } else {
            return factory.getBean(beanName, clazz);
        }
    }
}
