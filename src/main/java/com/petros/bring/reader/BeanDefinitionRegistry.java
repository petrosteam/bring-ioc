package com.petros.bring.reader;

import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Interface for classes that can store bean definitions loaded by BeanDefinitionReader
 */
public interface BeanDefinitionRegistry {

    /**
     * Place a bean definition by its name into registry
     * @param beanType - class of the bean
     * @param beanDefinition - bean definition object
     */
    void registerBeanDefinition(Class<?> beanType, BeanDefinition beanDefinition);

    /**
     * Return a bean definition by bean name
     * @param beanName - name of bean definition
     */
    BeanDefinition getBeanDefinition(String beanName);

    /**
     * Return a set of bean definitions by bean class
     * @param beanType - name of bean definition
     */
    Set<BeanDefinition> getBeanDefinitionsByType(Class<?> beanType);

    /**
     * Return all bean definition names stored in the registry
     */
    String[] getBeanDefinitionNames();

    PriorityQueue<BeanDefinition> getBeanDefinitions();

    <T> Optional<BeanDefinition> getPrimaryBeanDefinition(Class<T> beanType);
}
