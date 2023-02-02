package com.petros.bring.test.model;

import com.petros.bring.Application;
import com.petros.bring.bean.factory.BeanFactory;
import com.petros.bring.reader.BeanDefinitionReader;
import com.petros.bring.reader.BeanDefinitionRegistry;
import com.petros.bring.reader.impl.AnnotatedBeanDefinitionReader;
import com.petros.bring.reader.impl.BeanDefinitionImpl;
import com.petros.bring.reader.impl.BeanDefinitionRegistryImpl;

import java.util.Arrays;

public class Client {
    public static void main(String[] args) {
        BeanFactory beanFactory = Application.run("com.petros.bring");
        System.out.println(beanFactory.getBean(SimpleService.class));
        System.out.println(beanFactory.getAllBeans(Service.class));
    }
}