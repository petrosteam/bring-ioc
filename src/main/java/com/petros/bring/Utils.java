package com.petros.bring;

import com.petros.bring.exception.BeanDefinitionStoreException;

public final class Utils {
    public static Class<?> getClassByName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new BeanDefinitionStoreException("Can not find class for bean: " + className);
        }
    }
}
