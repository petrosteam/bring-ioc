package com.petros.bring.postprocessor;

import com.petros.bring.annotations.Autowired;
import com.petros.bring.bean.factory.BeanFactory;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * The type Autowire bean post processor. Wires bean fields marked with @Autowire annotation.
 */
public class AutowireFieldsBeanPostProcessor implements BeanPostProcessor{
    /**
     * PostProcessor which sets field up by Autowire annotation
     * @param beanType a class of a bean
     * @param obj an objects of a bean
     * @param beanFactory factory to get/create a bean
     */
    @Override
    public <T> void postProcessBeforeInitialization(Class<T> beanType, T obj, BeanFactory beanFactory) {
        Arrays.stream(beanType.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Autowired.class))
                .forEach(field -> performAutowire(field, obj, beanFactory));
    }

    private void performAutowire(Field field, Object bean, BeanFactory beanFactory)  {
        field.setAccessible(true);
        try {
            field.set(bean, beanFactory.getBean(field.getType()));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(String.format("Exception during autowire if the field %s of the object %s", field, bean), e);
        }
    }

}
