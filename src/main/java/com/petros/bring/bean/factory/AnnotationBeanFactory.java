package com.petros.bring.bean.factory;

import com.petros.bring.annotations.Component;
import com.petros.bring.exception.*;
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
@Slf4j
public class AnnotationBeanFactory implements BeanFactory {

    protected final BeanDefinitionRegistry registry;
    protected static final Map<String, Object> rootContextMap = new ConcurrentHashMap<>();

    public AnnotationBeanFactory(BeanDefinitionRegistry registry, List<BeanPostProcessor> beanPostProcessors) {
        this.registry = registry;
    }

    @Override
    public <T> T getBean(Class<T> beanType) throws NoSuchBeanException, NoUniqueBeanException {
        return getOptionalBean(beanType).orElseThrow(
                () -> new NoSuchBeanException("Bean with type %s not found".formatted(beanType.getName()))
        );
    }

    private <T> Optional<T> getOptionalBean(Class<T> beanType) {
        var matchingBeans = getAllBeans(beanType);
        if (matchingBeans.size() > 1) {
            return Optional.ofNullable(this.getPrimary(beanType, matchingBeans));
        }

        return matchingBeans.values().stream()
                .findFirst()
                .orElseGet(() -> createBean(beanType, beanDefinitions));
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

    public <T> T create(BeanDefinition beanDefinition) {
        var bean = this.<T>createBean(beanDefinition);
        rootContextMap.put(beanDefinition.getName(), bean);
        return bean;
    }

    public <T> Collection<T> create(Class<T> beanType) {
        var beanDefinitions = this.getBeanDefinitionsByType(beanType);
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
            if (rootContextMap.containsKey(beanDefinition.getName())) {
                return (T) getBean(beanDefinition.getName(), clazz);
            }
            if (beanDefinition.getDependsOn() != null && beanDefinition.getDependsOn().length != 0) {
                var beansByType = new HashMap<>();
                for (String dependsOnName : beanDefinition.getDependsOn()) {
                    Class<?> bClass = getClassByName(dependsOnName);
//                    if (!rootContextMap.containsKey(dependsOnName)) {
//                        var beanDefinitionToCreate = registry.getBeanDefinition(dependsOnName);
//                        beanToInject = this.create(beanDefinitionToCreate);
//                    } else {
//                        beanToInject = rootContextMap.get(dependsOnName);
//                    }
                    //TODO: Please, refactor me
                    Object beanToInject = null;
                    if (!this.getOptionalBean(bClass).isPresent()) {
                        beanToInject =
                                this.getBeanDefinitionsByType(bClass).stream()
                                        .findFirst()
                                        .map(this::create)
                                        .orElseThrow(
                                                () -> new BeanCreationException("Could not create bean with type: " + bClass.getName())
                                        );
                    } else {
                        beanToInject = this.getOptionalBean(bClass).get();
                    }
                    beansByType.put(bClass, beanToInject);
                }
                return (T) clazz.getConstructor(beansByType.keySet().toArray(new Class<?>[]{}))
                        .newInstance(beansByType.values().toArray());
            } else {
                return (T) clazz.getConstructor().newInstance();
            }
            if (beanDefinition.getScope().equals(Scope.PROTOTYPE)) {
                return obj;
            }
            rootContextMap.put(beanDefinition.getName(), obj);
            return obj;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new BeanCreationException(beanDefinition, e);
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

    @Override
    public Map<String, Object> getRootMap() {
        return rootContextMap;
    }
}
