package com.pertos.bring.test.model;

import com.pertos.bring.reader.BeanDefinitionReader;
import com.pertos.bring.reader.BeanDefinitionRegistry;
import com.pertos.bring.reader.impl.AnnotatedBeanDefinitionReader;
import com.pertos.bring.reader.impl.BeanDefinitionImpl;
import com.pertos.bring.reader.impl.BeanDefinitionRegistryImpl;

import java.util.Arrays;

public class Client {
    public static void main(String[] args) {
        BeanDefinitionRegistry beanDefinitionRegistry = new BeanDefinitionRegistryImpl();
        BeanDefinitionReader beanDefinitionReader = new AnnotatedBeanDefinitionReader(beanDefinitionRegistry);
        beanDefinitionReader.loadBeanDefinitions("com.pertos.bring");
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
