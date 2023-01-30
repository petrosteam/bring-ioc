package com.petros.bring.reader;

import com.petros.bring.exception.BeanDefinitionStoreException;

public interface BeanDefinitionReader {

    int loadBeanDefinitions(String location) throws BeanDefinitionStoreException;
    BeanDefinitionRegistry getBeanDefinitionRegistry();
}
