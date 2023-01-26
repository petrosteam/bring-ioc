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


    public BeanDefinitionRegistryImpl() {
        beanDefinitionMap = new HashMap<>();
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
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

    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitionMap.keySet().toArray(String[]::new);
    }
}
