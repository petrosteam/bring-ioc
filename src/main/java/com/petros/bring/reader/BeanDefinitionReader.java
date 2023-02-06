package com.petros.bring.reader;

import com.petros.bring.exception.BeanDefinitionStoreException;

/**
 * Simple interface for bean definition readers that specifies load methods with location parameters.
 */
public interface BeanDefinitionReader {

    /**
     * Load bean definitions from the specified location
     * @param location - can be package name, java class, xml file or other resource
     * @return the number of created bean definitions
     * @throws BeanDefinitionStoreException if error appears during bean definition instantiation process
     */
    int loadBeanDefinitions(String location) throws BeanDefinitionStoreException;

    BeanDefinitionRegistry getBeanDefinitionRegistry();
}
