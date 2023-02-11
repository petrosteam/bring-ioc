package com.petros.bring.postprocessor;


import com.petros.bring.annotations.Autowired;
import com.petros.bring.annotations.Component;
import com.petros.bring.annotations.Qualifier;
import com.petros.bring.bean.factory.BeanFactory;
import com.petros.bring.context.AnnotationConfigApplicationContext;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * This post-processor is used for field injection
 * If object field is marked with {@link Autowired} annotation, bean is set to this field.
 * If object field also marked with {@link Qualifier} annotation, bean is found by value in this annotation and then
 * set to this field
 */
@Component
@Slf4j
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
            var fieldType = field.getType();
            var beanToInject = this.getDependencyForField(field);
            field.setAccessible(true);
            log.debug("Found {} to inject in {}", fieldType.getSimpleName(), beanClass.getSimpleName());
            try {
                field.set(bean, beanToInject);
            } catch (IllegalAccessException e) {
                log.error("Can not inject value in field {}", field.getName());
                throw new RuntimeException("Can not inject: %s to %s".formatted(beanToInject, bean));
            }
        }
        return bean;
    }

    private Object getDependencyForField(Field field) {
        if (field.isAnnotationPresent(Qualifier.class)) {
            var beanName = field.getAnnotation(Qualifier.class).value();
            return this.factory.getBean(beanName, field.getType());
        }
        return this.factory.getBean(field.getType());
    }
}
