package com.petros.bring.bean.factory;

import com.petros.bring.annotations.Component;
import com.petros.bring.annotations.Primary;
import com.petros.bring.exception.BeanCreationException;
import com.petros.bring.exception.NoSuchBeanException;
import com.petros.bring.exception.NoUniqueBeanException;
import com.petros.bring.postprocessor.BeanPostProcessor;
import com.petros.bring.reader.BeanDefinition;
import com.petros.bring.reader.BeanDefinitionRegistry;
import com.petros.bring.reader.Scope;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.petros.bring.Utils.getClassByName;

@Component(name = "beanFactory")
@Primary
@Slf4j
public class AnnotationBeanFactory implements BeanFactory {

    protected BeanDefinitionRegistry registry;
    protected static final Map<String, Object> rootContextMap = new ConcurrentHashMap<>();

    public AnnotationBeanFactory(BeanDefinitionRegistry registry) {
        this.registry = registry;
        rootContextMap.clear();
    }

    @Override
    public <T> T getBean(Class<T> beanType) throws NoSuchBeanException, NoUniqueBeanException {
        return getOptionalBean(beanType)
                .or(() -> this.getPrototypeBeanByType(beanType))
                .orElseThrow(() -> new NoSuchBeanException("Bean with type %s not found".formatted(beanType.getName()))
                );
    }

    public <T> Optional<T> getPrototypeBeanByType(Class<T> beanType) {
        return this.registry.getBeanDefinitionsByType(beanType)
                .stream()
                .filter(beanDefinition -> beanDefinition.getScope().equals(Scope.PROTOTYPE))
                .map(this::createBean)
                .map(beanType::cast)
                .findFirst();
    }

    private <T> Optional<T> getOptionalBean(Class<T> beanType) {
        var matchingBeans = getAllBeans(beanType);
        if (matchingBeans.size() > 1) {
            return registry.getPrimaryBeanDefinition(beanType)
                    .map(BeanDefinition::getName)
                    .map(rootContextMap::get)
                    .map(beanType::cast);
        }
        return matchingBeans.values().stream()
                .findFirst();
    }

    public <T> T create(BeanDefinition beanDefinition) {
        var bean = this.<T>createBean(beanDefinition);
        if (!beanDefinition.getScope().equals(Scope.PROTOTYPE)) {
            rootContextMap.put(beanDefinition.getName(), bean);
        }
        return bean;
    }

    public <T> Collection<T> create(Class<T> beanType) {
        var beanDefinitions = this.registry.getBeanDefinitionsByType(beanType);
        var definitionsMap = beanDefinitions.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        this::<T>createBean
                ));

        definitionsMap.values().forEach(bean -> this.postProcessBean(beanType, bean));
        definitionsMap.forEach((key, value) -> rootContextMap.put(key.getName(), value));
        return definitionsMap.values();
    }

    public void postProcessBean(Class<?> beanType, Object bean) {
        for (BeanPostProcessor postProcessor : getAllBeans(BeanPostProcessor.class).values()) {
            postProcessor.postProcessBeforeInitialization(beanType, bean);
            postProcessor.postProcessAfterInitialization(beanType, bean);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T createBean(BeanDefinition beanDefinition) {
        try {
            var clazz = getClassByName(beanDefinition.getBeanClassName());
            // We've created beans here (com/petros/bring/context/AnnotationConfigApplicationContext.java:19) but also
            // call this method recursively that causes creating duplicates
            if (rootContextMap.containsKey(beanDefinition.getName())) {
                return (T) getBean(beanDefinition.getName(), clazz);
            }
            T obj = null;
            if (beanDefinition.getDependsOn() != null && beanDefinition.getDependsOn().length != 0) {
                var beansByType = new HashMap<>();
                for (Class<?> dependsOnClass : beanDefinition.getDependsOn()) {
                    //TODO: Please, refactor me
                    Object beanToInject = null;
                    if (!this.getOptionalBean(dependsOnClass).isPresent()) {
                        beanToInject =
                                this.registry.getBeanDefinitionsByType(dependsOnClass).stream()
                                        .findFirst()
                                        .map(this::create)
                                        .orElseThrow(
                                                () -> new BeanCreationException("Could not create bean with type: " + dependsOnClass.getName())
                                        );
                    } else {
                        beanToInject = this.getOptionalBean(dependsOnClass).get();
                    }
                    beansByType.put(dependsOnClass, beanToInject);
                }
                return (T) clazz.getConstructor(beansByType.keySet().toArray(new Class<?>[]{}))
                        .newInstance(beansByType.values().toArray());
            } else {
                return (T) clazz.getConstructor().newInstance();
            }
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new BeanCreationException(beanDefinition, e);
        }
    }

    private void ensureBeanDefinitionsCreated(Set<BeanDefinition> beanDefinitions) {
        beanDefinitions.forEach(bd -> {
            try {
                getBean(Class.forName(bd.getBeanClassName()));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(String.format("Not able to find class %s", bd.getBeanClassName()), e);
            }
        });
    }


    @Override
    public <T> T getBean(String name, Class<T> beanType) throws NoSuchBeanException, NoUniqueBeanException {
        return rootContextMap.entrySet().stream()
                .filter(beanEntry -> name.equals(beanEntry.getKey()))
                .findFirst()
                .map(Map.Entry::getValue)
                .map(beanType::cast)
                .orElseThrow(() -> new NoSuchBeanException("No bean named " + name + " available"));
    }

    @Override
    public <T> Map<String, T> getAllBeans(Class<T> beanType) {
        return rootContextMap.entrySet().stream()
                .filter(entry -> beanType.isAssignableFrom(entry.getValue().getClass()))
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> beanType.cast(entry.getValue())));
    }
}