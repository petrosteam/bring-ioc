package com.petros.bring.reader.impl;

import com.petros.bring.annotations.Component;
import com.petros.bring.annotations.Configuration;
import com.petros.bring.annotations.Lazy;
import com.petros.bring.annotations.Primary;
import com.petros.bring.exception.BeanDefinitionStoreException;
import com.petros.bring.reader.BeanDefinition;
import com.petros.bring.reader.BeanDefinitionReader;
import com.petros.bring.reader.BeanDefinitionRegistry;
import com.petros.bring.reader.Scope;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.reflections.Reflections;

import java.util.Set;


/**
 * The type Annotated bean definition reader.
 * scans class from provided package and
 * registers them into BeanDefinitionRegistry
 */
public class AnnotatedBeanDefinitionReader implements BeanDefinitionReader {

    private final BeanDefinitionRegistry beanDefinitionRegistry;

    /**
     * Instantiates a new Annotated bean definition reader.
     *
     * @param beanDefinitionRegistry the bean definition registry
     */
    public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry) {
        Assert.assertNotNull(beanDefinitionRegistry);
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }


    @Override
    public int loadBeanDefinitions(String location) throws BeanDefinitionStoreException {
        Reflections reflections = new Reflections(location);
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Component.class);
        Set<Class<?>> configurationClasses = reflections.getTypesAnnotatedWith(Configuration.class);
        annotatedClasses
                .forEach(clazz -> beanDefinitionRegistry.registerBeanDefinition(clazz, createBeanDefinition(clazz)));
        configurationClasses
                .forEach(clazz -> beanDefinitionRegistry.registerBeanDefinition(clazz, createBeanDefinition(clazz)));
        return beanDefinitionRegistry.getBeanDefinitionNames().length;
    }

    public BeanDefinitionRegistry getBeanDefinitionRegistry() {
        return beanDefinitionRegistry;
    }

    private BeanDefinition createBeanDefinition(Class<?> aClass) {
        return BeanDefinitionImpl.BeanDefinitionBuilder.newInstance()
                .withBeanClassName(aClass.getName())
                .withName(getBeanName(aClass))
                .withScope(getBeanScope(aClass))
                .withIsLazy(isLazy(aClass))
                .withIsPrimary(isPrimary(aClass))
                .withIsConfiguration(isConfiguration(aClass))
                .createBeanDefinitionImpl();
    }

    private boolean isPrimary(Class<?> aClass) {
        return aClass.isAnnotationPresent(Primary.class);
    }

    private boolean isLazy(Class<?> aClass) {
        return aClass.isAnnotationPresent(Lazy.class);
    }

    private boolean isConfiguration(Class<?> aClass) {
        return aClass.isAnnotationPresent(Configuration.class);
    }

    private Scope getBeanScope(Class<?> aClass) {
        if (aClass.isAnnotationPresent(Configuration.class)) {
            return Scope.SINGLETON;
        }
        return aClass.getAnnotation(Component.class).scope();
    }

    private String getBeanName(Class<?> aClass) {
        String name = null;
        if (aClass.isAnnotationPresent(Component.class)) {
            name = aClass.getAnnotation(Component.class).name();
        }
        return StringUtils.isEmpty(name) ? StringUtils.uncapitalize(aClass.getSimpleName()) : name;
    }

}
