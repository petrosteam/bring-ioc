package com.petros.bring.reader.impl;

import com.petros.bring.annotations.Component;
import com.petros.bring.exception.BeanDefinitionOverrideException;
import com.petros.bring.exception.NoUniqueBeanException;
import com.petros.bring.reader.BeanDefinition;
import com.petros.bring.reader.BeanDefinitionRegistry;
import com.petros.bring.reader.Scope;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Bean definition registry.
 * holds a map of names - BeanDefinitions
 */
@Component(name = "registry")
public class BeanDefinitionRegistryImpl implements BeanDefinitionRegistry {
    private final Map<String, BeanDefinition> beanDefinitionMap;
    private final Map<Class<?>, BeanDefinition> beanDefinitionsByType;


    public BeanDefinitionRegistryImpl() {
        beanDefinitionMap = new HashMap<>();
        beanDefinitionsByType = new HashMap<>();
    }

    @Override
    public void registerBeanDefinition(Class<?> beanType, BeanDefinition beanDefinition) {
        validate(beanDefinition);
        beanDefinitionMap.put(beanDefinition.getName(), beanDefinition);
        beanDefinitionsByType.put(beanType, beanDefinition);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return beanDefinitionMap.get(beanName);
    }

    @Override
    public Set<BeanDefinition> getBeanDefinitionsByType(Class<?> beanType) {
        return beanDefinitionsByType.entrySet().stream()
                .filter(entry -> beanType.isAssignableFrom(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitionMap.keySet().toArray(String[]::new);
    }

    @Override
    public PriorityQueue<BeanDefinition> getBeanDefinitions() {
        PriorityQueue<BeanDefinition> queue = new PriorityQueue<>(Comparator.comparing(BeanDefinition::getOrder));
        queue.addAll(beanDefinitionMap.values());
        return queue;
    }

    void validate(BeanDefinition beanDefinition) {
        if (Scope.PROTOTYPE.equals(beanDefinition.getScope())) {
            return;
        }
        var existingBean = beanDefinitionMap.get(beanDefinition.getName());
        if (existingBean != null) {
            throw new BeanDefinitionOverrideException(String.format(
                    "Cannot register bean definition [%s] for bean '%s': There is already [%s] bound.",
                    beanDefinition, beanDefinition.getName(), existingBean
            ));
        }
    }

    public <T> Optional<BeanDefinition> getPrimaryBeanDefinition(Class<T> beanType) {
        Set<BeanDefinition> primaryBeanDefinitions = this.getBeanDefinitionsByType(beanType)
                .stream()
                .filter(BeanDefinition::isPrimary)
                .collect(Collectors.toSet());

        if (primaryBeanDefinitions.size() != 1) {
            throw new NoUniqueBeanException("Could not get bean of type " + beanType.getName() + ".");
        }

        return primaryBeanDefinitions.stream().findFirst();
    }
}
