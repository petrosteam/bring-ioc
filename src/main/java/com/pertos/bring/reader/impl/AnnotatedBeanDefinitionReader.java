package com.pertos.bring.reader.impl;

import com.pertos.bring.annotations.Component;
import com.pertos.bring.annotations.Configuration;
import com.pertos.bring.annotations.Lazy;
import com.pertos.bring.annotations.Primary;
import com.pertos.bring.exception.BeanDefinitionStoreException;
import com.pertos.bring.reader.BeanDefinition;
import com.pertos.bring.reader.BeanDefinitionReader;
import com.pertos.bring.reader.BeanDefinitionRegistry;
import com.pertos.bring.reader.Scope;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AnnotatedBeanDefinitionReader implements BeanDefinitionReader {

    private static AnnotatedBeanDefinitionReader instance = null;

    private BeanDefinitionRegistry beanDefinitionRegistry;

    private AnnotatedBeanDefinitionReader() {
    }

    public static synchronized AnnotatedBeanDefinitionReader getInstance() {
        if (instance == null) {
            instance = new AnnotatedBeanDefinitionReader();
        }
        return instance;
    }

    @Override
    public int loadBeanDefinitions(String location) throws BeanDefinitionStoreException {
        Reflections reflections = new Reflections(location);
        beanDefinitionRegistry = new BeanDefinitionRegistryImpl(new HashSet<>());
        beanDefinitionRegistry.registerBeanDefinitionAll(loadBeanDefinitionsByComponentTypes(reflections));
        beanDefinitionRegistry.registerBeanDefinitionAll(loadBeanDefinitionsByConfigClass(reflections));
        return beanDefinitionRegistry.getBeanDefinitionNames().length;
    }

    public BeanDefinitionRegistry getBeanDefinitionRegistry() {
        return beanDefinitionRegistry;
    }

    private Set<BeanDefinition> loadBeanDefinitionsByConfigClass(Reflections reflections) {
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Configuration.class);
        return annotatedClasses.stream()
                .flatMap(aClass -> Arrays.stream(aClass.getDeclaredMethods()))
                .filter(method -> method.isAnnotationPresent(Component.class))
                .map(this::createBeanDefinition)
                .collect(Collectors.toSet());
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

    private BeanDefinition createBeanDefinition(Method method) {
        return BeanDefinitionImpl.BeanDefinitionBuilder.newInstance()
                .withBeanClassName(method.getReturnType().getName())
                .withName(getBeanName(method))
                .withScope(getBeanScope(method))
                .withIsLazy(isLazy(method))
                .withIsPrimary(isPrimary(method))
                .createBeanDefinitionImpl();
    }

    private boolean isPrimary(Class<?> aClass) {
        return aClass.isAnnotationPresent(Primary.class);
    }

    private boolean isLazy(Class<?> aClass) {
        return aClass.isAnnotationPresent(Lazy.class);
    }

    private boolean isPrimary(Method method) {
        return method.isAnnotationPresent(Primary.class);
    }

    private boolean isLazy(Method method) {
        return method.isAnnotationPresent(Lazy.class);
    }

    private Scope getBeanScope(Method method) {
        return method.getAnnotation(Component.class).scope();
    }

    private String getBeanName(Method method) {
        String name = method.getAnnotation(Component.class).name();
        return StringUtils.isEmpty(name) ? StringUtils.uncapitalize(method.getReturnType().getSimpleName()) : name;
    }

    private Scope getBeanScope(Class<?> aClass) {
        return aClass.getAnnotation(Component.class).scope();
    }

    private String getBeanName(Class<?> aClass) {
        String name = aClass.getAnnotation(Component.class).name();
        return StringUtils.isEmpty(name) ? StringUtils.uncapitalize(aClass.getSimpleName()) : name;
    }

}
