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

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JavaBeanDefinitionReader implements BeanDefinitionReader {

    private final BeanDefinitionRegistry beanDefinitionRegistry;

    public JavaBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry) {
        Assert.assertNotNull(beanDefinitionRegistry);
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }


    /**
     * @param location defines lroot package for scan
     * @return number of bean definitions been loaded
     * @throws BeanDefinitionStoreException once any exception (doubtful)
     */
    @Override
    public int loadBeanDefinitions(String location) throws BeanDefinitionStoreException {
        Reflections reflections = new Reflections(location);
        beanDefinitionRegistry.registerBeanDefinitionAll(loadBeanDefinitionsByConfigClass(reflections));
        return beanDefinitionRegistry.getBeanDefinitionNames().length;
    }

    private Set<BeanDefinition> loadBeanDefinitionsByConfigClass(Reflections reflections) {
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Configuration.class);
        return annotatedClasses.stream()
                .flatMap(aClass -> Arrays.stream(aClass.getDeclaredMethods()))
                .filter(method -> method.isAnnotationPresent(Component.class))
                .map(this::createBeanDefinition)
                .collect(Collectors.toSet());
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


    /**
     * @return bean definition registry composed with the reader
     */
    @Override
    public BeanDefinitionRegistry getBeanDefinitionRegistry() {
        return this.beanDefinitionRegistry;
    }
}