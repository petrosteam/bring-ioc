package com.petros.bring.environment.convert;

import com.petros.bring.annotations.Component;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Utility class that helps to convert an object from one class to another like:
 * String to Double or String to Integer.
 * <p>
 * Further implementations of {@link TypeConverter} may contain String -> Array or String -> Map convertors.
 */
@Component
public class TypeConversionService {

    private static Map<Class<?>, Class<?>> primitiveTypeToWrapperMap;

    private TypeConverter typeConverter;

    public TypeConversionService(TypeConverter converter) {
        this.typeConverter = converter;
        primitiveTypeToWrapperMap = fillMap();
    }

    public Object convertValueIfPossible(Object value, Class<?> sourceType, Class<?> targetType) {

        if (String.class.isAssignableFrom(targetType)) {
            return value;
        } else {
            Class<?> finalTargetType = getObjectType(targetType);
            if (typeConverter.canConvert(sourceType, finalTargetType)) {
                return typeConverter.convert(value, sourceType, finalTargetType);
            }
            throw new TypeConversionException(sourceType.getName(), targetType.getName());
        }
    }

    private static Class<?> getObjectType(Class<?> targetType) {
        return (targetType.isPrimitive() && targetType != void.class ? primitiveTypeToWrapperMap.get(targetType) : targetType);
    }

    private static Map<Class<?>, Class<?>> fillMap() {
        Map<Class<?>, Class<?>> map = new IdentityHashMap<>(9);
        map.put(int.class, Integer.class);
        map.put(long.class, Long.class);
        map.put(double.class, Double.class);
        map.put(float.class, Float.class);
        map.put(byte.class, Byte.class);
        map.put(short.class, Short.class);

        return map;
    }

}
