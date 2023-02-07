package com.petros.bring.environment.convert;

import com.petros.bring.annotations.Component;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Implementation of {@link TypeConverter} that converts String value of any instance of Number
 * to required Class
 */
@Component
public class StringToNumberTypeConverter implements TypeConverter {

    @Override
    public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
        return Number.class.isAssignableFrom(targetType);
    }

    @Override
    public Number convert(Object value, Class<?> sourceType, Class<?> targetType) {
        return convertToNumber(String.valueOf(value), targetType);
    }

    public <T extends Number> T convertToNumber(String value, Class<?> targetType) {
        String trimmed = StringUtils.trim(value);

        if (Byte.class == targetType) {
            return (T) Byte.valueOf(trimmed);
        } else if (Short.class == targetType) {
            return (T) Short.valueOf(trimmed);
        } else if (Integer.class == targetType) {
            return (T) Integer.valueOf(trimmed);
        } else if (Long.class == targetType) {
            return (T) Long.valueOf(trimmed);
        } else if (BigInteger.class == targetType) {
            return (T) new BigInteger(trimmed);
        } else if (Float.class == targetType) {
            return (T) Float.valueOf(trimmed);
        } else if (Double.class == targetType) {
            return (T) Double.valueOf(trimmed);
        } else if (BigDecimal.class == targetType || Number.class == targetType) {
            return (T) new BigDecimal(trimmed);
        } else {
            throw new IllegalArgumentException(
                    "Cannot convert String [" + value + "] to target class [" + targetType.getName() + "]");
        }
    }

}
