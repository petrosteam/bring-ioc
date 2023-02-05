package com.petros.bring.postprocessor;

import com.petros.bring.annotations.Bean;
import com.petros.bring.annotations.Configuration;
import com.petros.bring.annotations.Order;
import com.petros.bring.bean.factory.BeanFactory;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import net.sf.cglib.proxy.MethodInterceptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Order(1)
public class ConfigurationBeanPostProcessor extends OrderedBeanDefinitionPostProcessor {

    public ConfigurationBeanPostProcessor(int order) {
        super(order);
    }

    @Override
    public Object postProcessAfterInitialization(Class<?> beanClass, Object bean, BeanFactory factory) {
        if (!beanClass.isAnnotationPresent(Configuration.class)) {
            return bean;
        }

        return bean;
        // TODO: 05.02.2023 Make class annotated @Configuration proxy. Methods should return singletons
//        return Enhancer.create(beanClass, (InvocationHandler) (proxy, method, args) ->
//                returnSingletonBeans(method, args, bean, factory));
    }

    private Object returnSingletonBeans(Method method, Object[] args, Object bean, BeanFactory factory) {
        if (method.isAnnotationPresent(Bean.class)) {
            Class<?> returnType = method.getReturnType();
            return factory.getBean(returnType);
        }
        try {
            return method.invoke(bean, args);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> void postProcessBeforeInitialization(Class<T> beanType, T obj, BeanFactory annotationBeanFactory) {

    }
}
