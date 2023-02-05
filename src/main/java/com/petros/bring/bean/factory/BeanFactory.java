package com.petros.bring.bean.factory;

import com.petros.bring.exception.NoSuchBeanException;
import com.petros.bring.exception.NoUniqueBeanException;

import java.util.Map;

public interface BeanFactory {

    <T> T getBean(Class<T> beanType) throws NoSuchBeanException, NoUniqueBeanException;

    <T> T getBean(String name, Class<T> beanType) throws NoSuchBeanException, NoUniqueBeanException;

    <T> Map<String, T> getAllBeans(Class<T> beanType);

    void registerBean(String name, Object bean);
}
