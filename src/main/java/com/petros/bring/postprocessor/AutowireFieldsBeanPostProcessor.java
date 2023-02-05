package com.petros.bring.postprocessor;

import com.petros.bring.annotations.Autowired;
import com.petros.bring.bean.factory.BeanFactory;
import com.petros.bring.context.AnnotationConfigApplicationContext;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * The type Autowire bean post processor.
 */
public class AutowireFieldsBeanPostProcessor implements BeanPostProcessor {

    private final BeanFactory factory;

    public AutowireFieldsBeanPostProcessor(AnnotationConfigApplicationContext beanFactory) {
        this.factory = beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Class<?> beanType, Object bean) {
        Arrays.stream(beanType.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Autowired.class))
                .forEach(field -> performAutowire(field, bean));
        return bean;
    }

    private void performAutowire(Field field, Object bean)  {
        field.setAccessible(true);
        try {
            field.set(bean, factory.getBean(field.getType()));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(String.format("Exception during autowire if the field %s of the object %s", field, bean), e);
        }
    }

}
