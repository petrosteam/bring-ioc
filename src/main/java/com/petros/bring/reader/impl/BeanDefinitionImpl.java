package com.petros.bring.reader.impl;

import com.petros.bring.reader.BeanDefinition;
import com.petros.bring.reader.Scope;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

import java.util.Arrays;
import java.util.Objects;

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