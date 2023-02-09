package com.petros.bring.postprocessor;


import com.petros.bring.annotations.Autowired;
import com.petros.bring.annotations.Component;
import com.petros.bring.annotations.Qualifier;
import com.petros.bring.bean.factory.AnnotationBeanFactory;
import com.petros.bring.bean.factory.BeanFactory;
import com.petros.bring.context.AnnotationConfigApplicationContext;

import java.lang.reflect.Field;
import java.util.Arrays;

@Component
public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {
    private final BeanFactory factory;

    public AutowiredAnnotationBeanPostProcessor(AnnotationConfigApplicationContext beanFactory) {
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
                field.set(bean, getDependencyForField(field, factory));
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Can not inject: %s to %s".formatted(beanToInject, bean));
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

    private Object getDependencyForField(Field field, BeanFactory beanFactory) {
        if (field.isAnnotationPresent(Qualifier.class)) {
            var beanName = field.getAnnotation(Qualifier.class).value();
            return beanFactory.getBean(beanName, field.getType());
        }
        return beanFactory.getBean(field.getType());
    }
}
