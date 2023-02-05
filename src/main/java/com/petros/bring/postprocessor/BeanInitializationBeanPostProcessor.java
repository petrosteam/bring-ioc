package com.petros.bring.postprocessor;

import com.petros.bring.annotations.Bean;
import com.petros.bring.annotations.Configuration;
import com.petros.bring.annotations.Order;
import com.petros.bring.bean.factory.BeanFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

@Order(2)
public class BeanInitializationBeanPostProcessor extends OrderedBeanDefinitionPostProcessor {
    public BeanInitializationBeanPostProcessor(int order) {
        super(order);
    }

    @Override
    public Object postProcessAfterInitialization(Class<?> beanClass, Object bean, BeanFactory factory) {
        if (!beanClass.isAnnotationPresent(Configuration.class)) {
            return bean;
        }
        Arrays.stream(beanClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .forEach(method -> {
                    Object[] parameters = new Object[method.getParameterCount()];
                    int counter = 0;
                    for (Class<?> parameterType : method.getParameterTypes()) {
                        parameters[counter++] = factory.getBean(parameterType);
                    }
                    try {
                        Object newBean = method.invoke(bean, parameters);
                        factory.registerBean(method.getName(), newBean);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });
        return bean;
    }

    @Override
    public <T> void postProcessBeforeInitialization(Class<T> beanType, T obj, BeanFactory annotationBeanFactory) {

    }
}
