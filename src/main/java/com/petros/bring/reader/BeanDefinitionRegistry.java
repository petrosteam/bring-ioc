package com.petros.bring.reader;

import java.util.Collection;
import java.util.Set;

public interface BeanDefinitionRegistry {

    void registerBeanDefinition(Class<?> beanType, BeanDefinition beanDefinition);

    void registerBeanDefinitionAll(Set<BeanDefinition> beanDefinitions);

    void removeBeanDefinition(String beanName);

    BeanDefinition getBeanDefinition(String beanName);

    Class<?> getBeanTypeByName(String name);

    Set<BeanDefinition> getBeanDefinitionByType(Class<?> beanType);

    String[] getBeanDefinitionNames();

    Collection<BeanDefinition> getBeanDefinitions();
}
