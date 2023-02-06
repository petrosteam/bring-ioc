package com.petros.bring.environment;

import com.petros.bring.annotations.Component;
import com.petros.bring.exception.UnsatisfiedPropertyValueException;

/**
 * Implementation of {@link PropertyResolver} designed to read key-value properties of String type in a format:
 * "${variableKey}" or with a default value if no such value in properties file:
 * "${variableKey:defaultPropertyValue}"
 */
@Component
public class ApplicationPropertyResolver implements PropertyResolver {

    private static final String PROPS_PREFIX = "${";
    private static final String PROPS_SUFFIX = "}";
    private static final String PROP_DEFAULT_VALUE_SEPARATOR = ":";

    private final ApplicationEnvironment applicationEnvironment;

    public ApplicationPropertyResolver(ApplicationEnvironment applicationEnvironment) {
        this.applicationEnvironment = applicationEnvironment;
    }

    @Override
    public boolean canHandle(String propertyValue) {
        return propertyValue.startsWith(PROPS_PREFIX) && propertyValue.endsWith(PROPS_SUFFIX);
    }

    @Override
    public PropertyData handle(String propertyValue, Class<?> clazz) {
        String value;
        String defaultValue = null;

        String rawValue = propertyValue.substring(2, propertyValue.length() - 1);
        int defaultValueIndex = rawValue.indexOf(PROP_DEFAULT_VALUE_SEPARATOR);
        if (defaultValueIndex != -1) {
            String[] array = rawValue.split(PROP_DEFAULT_VALUE_SEPARATOR);
            if (array.length > 2) {
                throw new UnsatisfiedPropertyValueException(propertyValue);
            }
            value = array[0];
            defaultValue = array[1];
        } else {
            value = rawValue;
        }

        var valueFromProperties = applicationEnvironment.getProperty(value);
        if (valueFromProperties != null) {
            value = valueFromProperties;
        } else if (defaultValue != null) {
            value = defaultValue;
        } else {
            throw new UnsatisfiedPropertyValueException(propertyValue);
        }
        return new PropertyData(value);
    }
}
