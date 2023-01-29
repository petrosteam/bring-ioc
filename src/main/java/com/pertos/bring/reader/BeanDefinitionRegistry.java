package com.pertos.bring.reader;

/**
 * Interface for classes that can store bean definitions loaded by BeanDefinitionReader
 */
import java.util.Set;

public interface BeanDefinitionRegistry {

    /**
     * Place a bean definition by its name into registry
     * @param beanName - name of bean definition
     * @param beanDefinition - bean definition object
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    void registerBeanDefinitionAll(Set<BeanDefinition> beanDefinitions);

    /**
     * Delete bean definition from the registry by name
     * @param beanName - name of the bean definition
     */
    void removeBeanDefinition(String beanName);

    /**
     * Return a bean definition by its name
     * @param beanName - name of bean definition
     */
    BeanDefinition getBeanDefinition(String beanName);

    /**
     * Return all bean definition names stored in the registry
     */
    String[] getBeanDefinitionNames();
}
