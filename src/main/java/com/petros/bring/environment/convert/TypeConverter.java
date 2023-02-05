package com.petros.bring.environment.convert;

/**
 * Interface that describes an ability of Class to be converted from one type to another
 */
public interface TypeConverter {

    boolean canConvert(Class<?> sourceType, Class<?> targetType);

    Object convert(Object value, Class<?> sourceType, Class<?> targetType);
}
