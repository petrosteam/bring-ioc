package com.petros.bring.postprocessor;

import com.petros.bring.annotations.Autowired;
import com.petros.bring.annotations.Component;
import com.petros.bring.bean.factory.BeanFactory;
import com.petros.bring.context.AnnotationConfigApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

/**
 * The type Autowire methods bean post processor. Wires bean method parameters marked with autowire.
 */
@Component
public class AutowireMethodsBeanPostProcessor implements BeanPostProcessor {

    private final BeanFactory factory;

    public AutowireMethodsBeanPostProcessor(AnnotationConfigApplicationContext beanFactory) {
        this.factory = beanFactory;
    }

    /**
     * Wires bean method parameters marked with autowire.
     * Wires only methods with one parameter.
     *
     * @param beanClass              type of bean
     * @param bean                   instance of a bean
     */
    @Override
    public Object postProcessBeforeInitialization(Class<?> beanClass, Object bean) {
        Arrays.stream(beanClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Autowired.class))
                .forEach(method -> wireMethod(method, bean));
        return bean;
    }

    private <T> void wireMethod(Method method, T obj) {
        Parameter[] parameters = method.getParameters();
        if (parameters.length != 1) {
            throw new RuntimeException(String.format("Unexpected number of parameters for the metod with Autowire " +
                    "annotation. One is expected but %s actual", parameters.length));
        }
        Object beanToWire = factory.getBean(parameters[0].getType());
        try {
            method.invoke(obj, beanToWire);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(String.format("Unexpected exception while wire object=%s, method=%s", obj, method.getName()), e);
        }
    }
}