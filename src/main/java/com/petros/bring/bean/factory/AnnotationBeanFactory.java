package com.petros.bring.bean.factory;

import com.petros.bring.exception.NoSuchBeanException;
import com.petros.bring.exception.NoUniqueBeanException;
import com.petros.bring.postprocessor.BeanPostProcessor;
import com.petros.bring.reader.BeanDefinition;
import com.petros.bring.reader.BeanDefinitionRegistry;
import com.petros.bring.reader.Scope;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class AnnotationBeanFactory implements BeanFactory {

    protected final BeanDefinitionRegistry registry;
    private final List<BeanPostProcessor> beanPostProcessors;
    protected static final Map<String, Object> rootContextMap = new ConcurrentHashMap<>();

    public AnnotationBeanFactory(BeanDefinitionRegistry registry, List<BeanPostProcessor> beanPostProcessors) {
        this.registry = registry;
        this.beanPostProcessors = beanPostProcessors;
    }

    @Override
    public <T> T getBean(Class<T> beanType) throws NoSuchBeanException, NoUniqueBeanException {
        Map<String, T> matchingBeans = getAllBeans(beanType);
        if (matchingBeans.size() > 1) {
            return getPrimaryRegisteredBean(beanType, matchingBeans);
        }

        return matchingBeans.values().stream()
                .findFirst()
                .orElseGet(() -> createBean(beanType));
    }

    private <T> T createBean(Class<T> beanType) {
        try {
            Set<BeanDefinition> beanDefinitionByType = registry.getBeanDefinitionByType(beanType);
            if (beanDefinitionByType.isEmpty()) {
                throw new NoSuchBeanException("Could not create bean of type " + beanType.getName());
            }
            BeanDefinition beanDefinition = beanDefinitionByType.size() > 1
                    ? getPrimaryBeanDefinition(beanType, beanDefinitionByType)
                    : beanDefinitionByType.stream().findFirst().get();

            T obj = null;
            if (beanDefinition.getDependsOn() != null) {
//                Map<String, Object> beansByName = new HashMap<>();
//                for (String dependsOnName : beanDefinition.getDependsOn()) {
//                    Class<?> bClass = Class.forName(beanDefinition.getBeanClassName());
//                    beansByName.put(dependsOnName, getBean(bClass));
//                }
                // check for circular dependencies
//                obj = aClass.getConstructor().newInstance() with non default constructor
            } else {
                obj = beanType.getConstructor().newInstance();
            }
            for (BeanPostProcessor postProcessor : beanPostProcessors) {
                postProcessor.postProcessBeforeInitialization(beanType, obj, this);
                postProcessor.postProcessAfterInitialization(beanType, obj);
            }
            if (beanDefinition.getScope().equals(Scope.PROTOTYPE)) {
                return obj;
            }
            rootContextMap.put(beanDefinition.getName(), obj);
            return obj;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> BeanDefinition getPrimaryBeanDefinition(Class<T> beanType, Set<BeanDefinition> beanDefinitionByType) {
        Set<BeanDefinition> primaryBeanDefinitions = beanDefinitionByType.stream()
                .filter(BeanDefinition::isPrimary)
                .collect(Collectors.toSet());
        if (primaryBeanDefinitions.size() != 1) {
            throw new NoUniqueBeanException("Could not create bean of type " + beanType.getName() + ".");
        }
        return primaryBeanDefinitions.stream().findFirst().get();
    }

    private <T> T getPrimaryRegisteredBean(Class<T> beanType, Map<String, T> matchingBeans) {
        Set<Map.Entry<String, T>> primaryMatchingBeans = matchingBeans.entrySet().stream()
                .filter(entry -> registry.getBeanDefinition(entry.getKey()).isPrimary())
                .collect(Collectors.toSet());
        if (primaryMatchingBeans.size() != 1) {
            throw new NoUniqueBeanException(beanType + " expecting matching bean but not found");
        }
        return primaryMatchingBeans.stream().findFirst().get().getValue();
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
