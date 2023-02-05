package com.petros.bring.postprocessor;

import com.petros.bring.annotations.Autowired;
import com.petros.bring.bean.factory.BeanFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

/**
 * The type Autowire methods bean post processor. Wires bean method parameters marked with autowire.
 */
public class AutowireMethodsBeanPostProcessor implements BeanPostProcessor {

    /**
     * Wires bean method parameters marked with autowire.
     * Wires only methods with one parameter.
     *
     * @param beanType              type of bean
     * @param obj                   instance of a bean
     * @param annotationBeanFactory factory to get bean by parameter type
     * @param <T>                   return bean instance
     */
    @Override
    public <T> void postProcessBeforeInitialization(Class<T> beanType, T obj, BeanFactory annotationBeanFactory) {
        Arrays.stream(beanType.getDeclaredMethods()).filter(method -> method.isAnnotationPresent(Autowired.class)).forEach(method -> wireMethod(method, obj, annotationBeanFactory));
    }

    private <T> void wireMethod(Method method, T obj, BeanFactory beanFactory) {
        Parameter[] parameters = method.getParameters();
        if (parameters.length != 1) {
            throw new RuntimeException(String.format("Unexpected number of parameters for the metod with Autowire annotation. One is expeced but %s actual", parameters.length));
        }
        Object beanToWire = beanFactory.getBean(parameters[0].getType());
        try {
            method.invoke(obj, beanToWire);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(String.format("Unexpected exception while wire object=%s, method=%s", obj, method.getName()), e);
        }
    }
}
