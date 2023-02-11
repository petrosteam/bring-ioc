package com.petros.bring.reader.impl;

import com.petros.bring.annotations.*;
import com.petros.bring.exception.BeanDefinitionStoreException;
import com.petros.bring.reader.BeanDefinition;
import com.petros.bring.reader.BeanDefinitionReader;
import com.petros.bring.reader.BeanDefinitionRegistry;
import com.petros.bring.reader.Scope;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

@Component
public class JavaBeanDefinitionReader implements BeanDefinitionReader {

    private final BeanDefinitionRegistry beanDefinitionRegistry;

    public JavaBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry) {
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
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Configuration.class);
        annotatedClasses.stream()
                .flatMap(aClass -> Arrays.stream(aClass.getDeclaredMethods()))
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .forEach(method -> beanDefinitionRegistry
                        .registerBeanDefinition(method.getReturnType(), createBeanDefinition(method)));
        return beanDefinitionRegistry.getBeanDefinitionNames().length;
    }

    private BeanDefinition createBeanDefinition(Method method) {
        return BeanDefinitionImpl.builder()
                .beanClassName(method.getReturnType().getName())
                .name(getBeanName(method))
                .scope(getBeanScope(method))
                .isLazy(isLazy(method))
                .isPrimary(isPrimary(method))
                .factoryBeanClass(method.getDeclaringClass())
                .factoryMethod(method)
                .dependsOn(getDependsOn(method))
                .build();
    }

    private Class<?>[] getDependsOn(Method method) {
        return method.getParameterTypes();
    }

    private boolean isPrimary(Method method) {
        return method.isAnnotationPresent(Primary.class);
    }

    private boolean isLazy(Method method) {
        return method.isAnnotationPresent(Lazy.class);
    }

    private Scope getBeanScope(Method method) {
        return method.getAnnotation(Bean.class).scope();
    }

    private String getBeanName(Method method) {
        String name = method.getAnnotation(Bean.class).name();
        return StringUtils.isEmpty(name) ? StringUtils.uncapitalize(method.getName()) : name;
    }


    /**
     * @return bean definition registry composed with the reader
     */
    @Override
    public BeanDefinitionRegistry getBeanDefinitionRegistry() {
        return this.beanDefinitionRegistry;
    }
}
