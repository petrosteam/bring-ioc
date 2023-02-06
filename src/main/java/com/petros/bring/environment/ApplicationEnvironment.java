package com.petros.bring.environment;

import com.petros.bring.annotations.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * {@link ApplicationEnvironment} - a class that contains in itself information about properties that may
 * be used during beans initialization or work of an application.
 *
 * applicationPropertiesMap - a key-value map where Key is a name of property and Value is String representation
 * of property that may be casted to required type with the help of one of {@link PropertyResolver} implementations.
 */
@Component
public class ApplicationEnvironment {

    public static final String BASE_APPLICATION_PROPERTIES_FILE = "application.properties";

    private static Map<String, String> applicationPropertiesMap;
//    private static Set<PropertyResolver> propertyResolvers;

    private final PropertyResolver propertyResolver;

    public ApplicationEnvironment(PropertyResolver propertyResolver) {
        this.propertyResolver = propertyResolver;
        readApplicationProperties();
    }

    private void readApplicationProperties() {
        var propertiesFile = ClassLoader.getSystemClassLoader()
                .getResource(BASE_APPLICATION_PROPERTIES_FILE);
        String path;
        if (propertiesFile == null) {
            applicationPropertiesMap = new HashMap<>();
            return;
        }
        path = propertiesFile.getPath();

        try (var props = new BufferedReader(new FileReader(path)).lines()) {
            applicationPropertiesMap = props.map(line -> line.split("="))
                    .collect(Collectors.toMap(k -> k[0], v -> v[1]));
        } catch (FileNotFoundException e) {
            applicationPropertiesMap = new HashMap<>();
        }
    }

    public String getProperty(String propertyName) {
        return applicationPropertiesMap.get(propertyName);
    }

    public PropertyResolver getPropertyResolver() {
        return propertyResolver;
    }
}
