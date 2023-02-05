package com.petros.bring.reader.impl;

import com.petros.bring.reader.BeanDefinition;
import com.petros.bring.reader.Scope;

import java.util.Arrays;
import java.util.Objects;

public class BeanDefinitionImpl implements BeanDefinition {
    private String name;
    private String beanClassName;
    private Scope scope;
    private boolean isLazy;
    private boolean isPrimary;
    private Class<?>[] dependsOn;


    public BeanDefinitionImpl(String name, String beanClassName, Scope scope, boolean isLazy, boolean isPrimary, Class<?>[] dependsOn) {
        this.name = name;
        this.beanClassName = beanClassName;
        this.scope = scope;
        this.isLazy = isLazy;
        this.isPrimary = isPrimary;
        this.dependsOn = dependsOn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public void setLazy(boolean lazy) {
        isLazy = lazy;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public void setDependsOn(Class<?>[] dependsOn) {
        this.dependsOn = dependsOn;
    }

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeanDefinitionImpl that = (BeanDefinitionImpl) o;
        return beanClassName.equals(that.beanClassName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beanClassName);
    }

    @Override
    public String toString() {
        return "BeanDefinitionImpl{" +
                "name='" + name + '\'' +
                ", beanClassName='" + beanClassName + '\'' +
                ", scope=" + scope +
                ", isLazy=" + isLazy +
                ", isPrimary=" + isPrimary +
                ", dependsOn=" + Arrays.toString(dependsOn) +
                '}';
    }

    public static class BeanDefinitionBuilder {

        private String name;
        private String beanClassName;
        private Scope scope;
        private boolean isLazy;
        private boolean isPrimary;
        private Class<?>[] dependsOn;

        public static BeanDefinitionBuilder newInstance() {
            return new BeanDefinitionBuilder();
        }

        public BeanDefinitionBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public BeanDefinitionBuilder withBeanClassName(String beanClassName) {
            this.beanClassName = beanClassName;
            return this;
        }

        public BeanDefinitionBuilder withScope(Scope scope) {
            this.scope = scope;
            return this;
        }

        public BeanDefinitionBuilder withIsLazy(boolean isLazy) {
            this.isLazy = isLazy;
            return this;
        }

        public BeanDefinitionBuilder withIsPrimary(boolean isPrimary) {
            this.isPrimary = isPrimary;
            return this;
        }

        public BeanDefinitionBuilder withDependsOn(Class<?>[] dependsOn) {
            this.dependsOn = dependsOn;
            return this;
        }

        public BeanDefinitionImpl createBeanDefinitionImpl() {
            return new BeanDefinitionImpl(name, beanClassName, scope, isLazy, isPrimary, dependsOn);
        }
    }

}