package com.petros.bring.environment;

/**
 * Object to store property value
 */
public class PropertyData {
    private final Object value;

    public PropertyData(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}
