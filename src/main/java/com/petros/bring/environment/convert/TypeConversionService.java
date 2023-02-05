package com.petros.bring.environment.convert;

import org.junit.Assert;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Utility class that helps to convert an object from one class to another like:
 * String to Double or String to Integer.
 * <p>
 * Further implementations of {@link TypeConverter} may contain String -> Array or String -> Map convertors.
 */
public class TypeConversionService {

    private static Map<Class<?>, Class<?>> primitiveTypeToWrapperMap;
    private static Set<TypeConverter> converters;

    public TypeConversionService(Set<TypeConverter> converters) {
        TypeConversionService.converters = converters;
        primitiveTypeToWrapperMap = fillMap();
    }

    public static Object convertValueIfPossible(Object value, Class<?> sourceType, Class<?> targetType) {
        Assert.assertNotNull(sourceType);
        Assert.assertNotNull(targetType);

        if (String.class.isAssignableFrom(targetType)) {
            return value;
        } else {
            Class<?> finalTargetType = getObjectType(targetType);
            return getConverter(sourceType, finalTargetType).convert(value, sourceType, finalTargetType);
        }
    }

    private static Class<?> getObjectType(Class<?> targetType) {
        return (targetType.isPrimitive() && targetType != void.class ? primitiveTypeToWrapperMap.get(targetType) : targetType);
    }

    public static TypeConverter getConverter(Class<?> sourceType, Class<?> targetType) {
        return converters.stream()
                .filter(converter -> converter.canConvert(sourceType, targetType))
                .findAny()
                .orElseThrow(() -> new TypeConversionException(sourceType.getName(), targetType.getName()));
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
