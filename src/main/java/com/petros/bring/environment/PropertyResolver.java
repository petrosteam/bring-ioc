package com.petros.bring.environment;

/**
 * Interface that describes an ability of class to get property value by its name.
 */
public interface PropertyResolver {

    boolean canHandle(String propertyValue);

    PropertyData handle(String propertyValue, Class<?> clazz);
}
