package com.pertos.bring.reader;

import java.util.Set;

public interface BeanDefinitionRegistry {

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    void registerBeanDefinitionAll(Set<BeanDefinition> beanDefinitions);

    void removeBeanDefinition(String beanName);

    BeanDefinition getBeanDefinition(String beanName);

    String[] getBeanDefinitionNames();
}
