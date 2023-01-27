package com.pertos.bring.reader.impl;

import com.pertos.bring.reader.BeanDefinition;
import com.pertos.bring.reader.BeanDefinitionRegistry;

import java.util.Set;

public class BeanDefinitionRegistryImpl implements BeanDefinitionRegistry {

    private final Set<BeanDefinition> beanDefinitionContext;

    public BeanDefinitionRegistryImpl(Set<BeanDefinition> beanDefinitionContext) {
        this.beanDefinitionContext = beanDefinitionContext;
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionContext.add(beanDefinition);
    }

    public void registerBeanDefinitionAll(Set<BeanDefinition> beanDefinitions) {
        beanDefinitionContext.addAll(beanDefinitions);
    }

    @Override
    public void removeBeanDefinition(String beanName) {
        beanDefinitionContext.removeIf(bd -> bd.getName().equals(beanName));
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return beanDefinitionContext.stream().filter(bd -> bd.getName().equals(beanName)).findFirst().orElseThrow();
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitionContext.stream().map(BeanDefinition::getName).toArray(String[]::new);
    }
}
