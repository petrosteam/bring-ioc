package com.petros.bring.test.model;

import com.petros.bring.reader.BeanDefinitionReader;
import com.petros.bring.reader.BeanDefinitionRegistry;
import com.petros.bring.reader.impl.AnnotatedBeanDefinitionReader;
import com.petros.bring.reader.impl.BeanDefinitionImpl;
import com.petros.bring.reader.impl.BeanDefinitionRegistryImpl;

import java.util.Arrays;

public class Client {
    public static void main(String[] args) {
        BeanDefinitionRegistry beanDefinitionRegistry = new BeanDefinitionRegistryImpl();
        BeanDefinitionReader beanDefinitionReader = new AnnotatedBeanDefinitionReader(beanDefinitionRegistry);
        beanDefinitionReader.loadBeanDefinitions("com.petros.bring");
        System.out.println(Arrays.toString(beanDefinitionRegistry.getBeanDefinitionNames()));

        beanDefinitionRegistry
                .registerBeanDefinition("newComer",
                        BeanDefinitionImpl.BeanDefinitionBuilder.newInstance()
                                .withBeanClassName("className")
                                .withName("newComer")
                                .createBeanDefinitionImpl());

        System.out.println(Arrays.toString(beanDefinitionRegistry.getBeanDefinitionNames()));

        beanDefinitionRegistry
                .registerBeanDefinition("newComer",
                        BeanDefinitionImpl.BeanDefinitionBuilder.newInstance()
                                .withBeanClassName("className")
                                .withName("newComer")
                                .createBeanDefinitionImpl());
        System.out.println(Arrays.toString(beanDefinitionRegistry.getBeanDefinitionNames()));

    }
}