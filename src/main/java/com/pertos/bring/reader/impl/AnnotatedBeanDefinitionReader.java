package com.pertos.bring.reader.impl;

import com.pertos.bring.annotations.Component;
import com.pertos.bring.annotations.Lazy;
import com.pertos.bring.annotations.Primary;
import com.pertos.bring.exception.BeanDefinitionStoreException;
import com.pertos.bring.reader.BeanDefinition;
import com.pertos.bring.reader.BeanDefinitionReader;
import com.pertos.bring.reader.BeanDefinitionRegistry;
import com.pertos.bring.reader.Scope;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.reflections.Reflections;

import java.util.Set;
import java.util.stream.Collectors;


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
        beanDefinitionRegistry.registerBeanDefinitionAll(loadBeanDefinitionsByComponentTypes(reflections));
        return beanDefinitionRegistry.getBeanDefinitionNames().length;
    }

    public BeanDefinitionRegistry getBeanDefinitionRegistry() {
        return beanDefinitionRegistry;
    }

    private Set<BeanDefinition> loadBeanDefinitionsByComponentTypes(Reflections reflections) {
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Component.class);
        return annotatedClasses.stream()
                .map(this::createBeanDefinition)
                .collect(Collectors.toSet());
    }

    private BeanDefinition createBeanDefinition(Class<?> aClass) {
        return BeanDefinitionImpl.BeanDefinitionBuilder.newInstance()
                .withBeanClassName(aClass.getName())
                .withName(getBeanName(aClass))
                .withScope(getBeanScope(aClass))
                .withIsLazy(isLazy(aClass))
                .withIsPrimary(isPrimary(aClass))
                .createBeanDefinitionImpl();
    }

    private boolean isPrimary(Class<?> aClass) {
        return aClass.isAnnotationPresent(Primary.class);
    }

    private boolean isLazy(Class<?> aClass) {
        return aClass.isAnnotationPresent(Lazy.class);
    }

    private Scope getBeanScope(Class<?> aClass) {
        return aClass.getAnnotation(Component.class).scope();
    }

    private String getBeanName(Class<?> aClass) {
        String name = aClass.getAnnotation(Component.class).name();
        return StringUtils.isEmpty(name) ? StringUtils.uncapitalize(aClass.getSimpleName()) : name;
    }

}
