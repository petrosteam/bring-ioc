package com.petros.bring.reader.impl;

import com.petros.bring.annotations.Component;
import com.petros.bring.annotations.Lazy;
import com.petros.bring.annotations.Primary;
import com.petros.bring.exception.BeanDefinitionStoreException;
import com.petros.bring.exception.ClassConctructorException;
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
        annotatedClasses
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
                .withDependsOn(getDependsOn(aClass))
                .createBeanDefinitionImpl();
    }

    private Class<?>[] getDependsOn(Class<?> aClass) {
        var constructors = aClass.getDeclaredConstructors();
        var constrCount = constructors.length;
        if (constrCount > 1) {
            throw new ClassConctructorException("Class have more than one constructor");
        } else if (constrCount==0) {
            throw new ClassConctructorException("Class have not public constructor");
        }
        var constructorParamsList = Arrays.stream(Arrays.stream(constructors)
                        .map(Constructor::getParameterTypes)
                        .findAny()
                        .orElseThrow())
                .toList();
        if (constructorParamsList.size() == 0) {
            return null;
        }
        var classesArray = new Class<?>[constructorParamsList.size()];
        return constructorParamsList.toArray(classesArray);
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
