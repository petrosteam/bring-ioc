package com.petros.bring.reader.impl;

import com.petros.bring.annotations.Component;
import com.petros.bring.annotations.Configuration;
import com.petros.bring.annotations.Primary;
import com.petros.bring.exception.BeanDefinitionStoreException;
import com.petros.bring.exception.ClassConctructorException;
import com.petros.bring.reader.BeanDefinition;
import com.petros.bring.reader.BeanDefinitionReader;
import com.petros.bring.reader.BeanDefinitionRegistry;
import com.petros.bring.reader.Scope;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.Set;


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
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }


    @Override
    public int loadBeanDefinitions(String location) throws BeanDefinitionStoreException {
        Reflections reflections = new Reflections(location);
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Component.class);
        annotatedClasses.addAll(reflections.getTypesAnnotatedWith(Configuration.class));
        annotatedClasses
                .forEach(clazz -> beanDefinitionRegistry.registerBeanDefinition(clazz, createBeanDefinition(clazz)));
        return beanDefinitionRegistry.getBeanDefinitionNames().length;
    }

    public BeanDefinitionRegistry getBeanDefinitionRegistry() {
        return beanDefinitionRegistry;
    }

    private BeanDefinition createBeanDefinition(Class<?> aClass) {
        return BeanDefinitionImpl.builder()
                .beanClassName(aClass.getName())
                .name(getBeanName(aClass))
                .scope(getBeanScope(aClass))
                .isPrimary(isPrimary(aClass))
                .dependsOn(getDependsOn(aClass))
                .order(getOrder(aClass))
                .build();
    }

    private int getOrder(Class<?> aClass) {
        return aClass.isAnnotationPresent(Configuration.class) ? 1 : 99;
    }

    private Class<?>[] getDependsOn(Class<?> aClass) {
        var constructors = aClass.getDeclaredConstructors();

        var constrCount = constructors.length;
        if (constrCount > 1) {
            throw new ClassConctructorException(aClass.getName() + " Class has more than one constructor");
        } else if (constrCount == 0) {
            throw new ClassConctructorException(aClass.getName() + " Class has not public constructor");
        }

        var constructor = constructors[0];

        var constructorParamsList = Arrays.asList(constructor.getParameterTypes());

        if (constructorParamsList.size() == 0) {
            return null;
        }

        return constructorParamsList.toArray(new Class<?>[constructorParamsList.size()]);
    }

    private boolean isPrimary(Class<?> aClass) {
        return aClass.isAnnotationPresent(Primary.class);
    }

    private Scope getBeanScope(Class<?> aClass) {
        return aClass.getAnnotation(Component.class) != null
                ? aClass.getAnnotation(Component.class).scope()
                : Scope.SINGLETON;
    }

    private String getBeanName(Class<?> aClass) {
        String name = aClass.getAnnotation(Component.class) != null
                ? aClass.getAnnotation(Component.class).name()
                : aClass.getAnnotation(Configuration.class).name();
        return StringUtils.isEmpty(name) ? StringUtils.uncapitalize(aClass.getSimpleName()) : name;
    }

}
