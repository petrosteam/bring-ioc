package com.pertos.bring.test.model;

import com.pertos.bring.reader.BeanDefinitionReader;
import com.pertos.bring.reader.impl.AnnotatedBeanDefinitionReader;
import com.pertos.bring.reader.impl.BeanDefinitionImpl;

import java.util.Arrays;

public class Client {
    public static void main(String[] args) {
        BeanDefinitionReader beanDefinitionReader = AnnotatedBeanDefinitionReader.getInstance();
        beanDefinitionReader.loadBeanDefinitions("com.pertos.bring");
        System.out.println(Arrays.toString(beanDefinitionReader.getBeanDefinitionRegistry().getBeanDefinitionNames()));

        beanDefinitionReader.getBeanDefinitionRegistry()
                .registerBeanDefinition("newComer",
                        BeanDefinitionImpl.BeanDefinitionBuilder.newInstance()
                                .withBeanClassName("className")
                                .withName("newComer")
                                .createBeanDefinitionImpl());

        System.out.println(Arrays.toString(beanDefinitionReader.getBeanDefinitionRegistry().getBeanDefinitionNames()));

        beanDefinitionReader.getBeanDefinitionRegistry()
                .registerBeanDefinition("newComer",
                        BeanDefinitionImpl.BeanDefinitionBuilder.newInstance()
                                .withBeanClassName("className")
                                .withName("newComer")
                                .createBeanDefinitionImpl());
        System.out.println(Arrays.toString(beanDefinitionReader.getBeanDefinitionRegistry().getBeanDefinitionNames()));

    }
}
