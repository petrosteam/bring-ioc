package com.pertos.bring.context;

import com.pertos.bring.exception.NoSuchBeanException;
import com.pertos.bring.exception.NoUniqueBeanException;

import java.util.Map;

public interface ApplicationContext {

    <T> T getBean(Class<T> beanType) throws NoSuchBeanException, NoUniqueBeanException;

    <T> T getBean(String name, Class<T> beanType) throws NoSuchBeanException, NoUniqueBeanException;

    <T> Map<String, T> getAllBeans(Class<T> beanType);
}
