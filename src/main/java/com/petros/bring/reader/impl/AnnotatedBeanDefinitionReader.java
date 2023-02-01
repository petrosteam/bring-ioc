package com.petros.bring.reader.impl;

import com.petros.bring.annotations.Component;
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

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Annotated bean definition reader.
 * scans class from provided package and
 * registers them into BeanDefinitionRegistry
 */
@Component
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
                .withDependsOn(getDependsOn(aClass))
                .createBeanDefinitionImpl();
    }

    private String[] getDependsOn(Class<?> aClass) {
        return Arrays.stream(aClass.getConstructors())
                .findAny()
                .map(Constructor::getParameterTypes)
                .map(Arrays::stream)
                .map(stream -> stream.map(clazz -> clazz.getName()).toList().toArray(new String[]{}))
                .orElse(new String[]{});
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
