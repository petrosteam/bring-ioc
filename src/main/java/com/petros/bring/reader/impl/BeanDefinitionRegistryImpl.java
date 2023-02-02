package com.petros.bring.reader.impl;

import com.petros.bring.reader.BeanDefinition;
import com.petros.bring.reader.BeanDefinitionRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The type Bean definition registry.
 * holds a map of names - BeanDefinitions
 */
public class BeanDefinitionRegistryImpl implements BeanDefinitionRegistry {
    private final Map<String, BeanDefinition> beanDefinitionMap;
    private final Map<Class<?>, BeanDefinition> beanDefinitionsByType;


    public BeanDefinitionRegistryImpl() {
        beanDefinitionMap = new HashMap<>();
        beanDefinitionsByType = new HashMap<>();
    }

    @Override
    public void registerBeanDefinition(Class<?> beanType, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanDefinition.getName(), beanDefinition);
        beanDefinitionsByType.put(beanType, beanDefinition);
    }

    public void registerBeanDefinitionAll(Set<BeanDefinition> beanDefinitions) {
        beanDefinitionMap.putAll(beanDefinitions.stream()
                .collect(Collectors.toMap(BeanDefinition::getName, Function.identity())));
    }

    @Override
    public void removeBeanDefinition(String beanName) {
        beanDefinitionMap.remove(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return beanDefinitionMap.get(beanName);
    }

    public Class<?> getBeanTypeByName(String name) {
        return beanDefinitionsByType.entrySet().stream()
                .filter(entry -> entry.getValue().getName().equals(name))
                .map(Map.Entry::getKey)
                .findFirst().orElseThrow();
    }

    @Override
    public Set<BeanDefinition> getBeanDefinitionByType(Class<?> beanType) {
        return beanDefinitionsByType.entrySet().stream()
                .filter(entry -> entry.getKey().isAssignableFrom(beanType))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitionMap.keySet().toArray(String[]::new);
    }
}
