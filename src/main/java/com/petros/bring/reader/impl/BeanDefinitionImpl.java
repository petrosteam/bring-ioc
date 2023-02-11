package com.petros.bring.reader.impl;

import com.petros.bring.reader.BeanDefinition;
import com.petros.bring.reader.Scope;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.Method;

@EqualsAndHashCode
@ToString
@Setter
@AllArgsConstructor
@Builder
public class BeanDefinitionImpl implements BeanDefinition {
    private String name;
    private String beanClassName;
    private Scope scope;
    private boolean isLazy;
    private boolean isPrimary;
    private Class<?>[] dependsOn;
    private Class<?> factoryBeanClass;
    private Method factoryMethod;
    @Builder.Default
    private int order = 99;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getBeanClassName() {
        return beanClassName;
    }

    @Override
    public Scope getScope() {
        return scope;
    }

    @Override
    public boolean isLazy() {
        return isLazy;
    }

    @Override
    public boolean isPrimary() {
        return isPrimary;
    }

    @Override
    public Class<?>[] getDependsOn() {
        return dependsOn;
    }

    @Override
    public Class<?> getFactoryBeanClass() {
        return factoryBeanClass;
    }

    @Override
    public Method getFactoryMethod() {
        return factoryMethod;
    }

    @Override
    public int getOrder() {
        return order;
    }
}
