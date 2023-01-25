package com.pertos.bring.reader;

import com.pertos.bring.exception.BeanDefinitionStoreException;

public interface BeanDefinitionReader {

    int loadBeanDefinitions(String location) throws BeanDefinitionStoreException;
    BeanDefinitionRegistry getBeanDefinitionRegistry();
}
