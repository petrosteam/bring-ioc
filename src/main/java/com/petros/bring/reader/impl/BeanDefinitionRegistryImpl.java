package com.petros.bring.reader.impl;

import com.petros.bring.annotations.Component;
import com.petros.bring.exception.BeanDefinitionOverrideException;
import com.petros.bring.reader.BeanDefinition;
import com.petros.bring.reader.BeanDefinitionRegistry;
import com.petros.bring.reader.Scope;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The type Bean definition registry.
 * holds a map of names - BeanDefinitions
 */
@Component
public class BeanDefinitionRegistryImpl implements BeanDefinitionRegistry {
    private final Map<String, BeanDefinition> beanDefinitionMap;
    private final Map<Class<?>, BeanDefinition> beanDefinitionsByType;


    public BeanDefinitionRegistryImpl() {
        beanDefinitionMap = new HashMap<>();
        beanDefinitionsByType = new HashMap<>();
    }

    @Override
    public void registerBeanDefinition(Class<?> beanType, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanDefinition.getName(), beanDefinition);
        beanDefinitionsByType.put(beanType, beanDefinition);

        public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        validate(beanName);
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    public void registerBeanDefinitionAll(Set<BeanDefinition> beanDefinitions) {
        beanDefinitions.forEach(beanDefinition -> registerBeanDefinition(beanDefinition.getName(),
                beanDefinition));
    }

    @Override
    public void removeBeanDefinition(String beanName) {
        beanDefinitionMap.remove(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return beanDefinitionMap.get(beanName);
    }

    public Class<?> getBeanTypeByName(String name) {
        return beanDefinitionsByType.entrySet().stream()
                .filter(entry -> entry.getValue().getName().equals(name))
                .map(Map.Entry::getKey)
                .findFirst().orElseThrow();
    }

    @Override
    public Set<BeanDefinition> getBeanDefinitionByType(Class<?> beanType) {
        return beanDefinitionsByType.entrySet().stream()
                .filter(entry -> entry.getKey().isAssignableFrom(beanType))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitionMap.keySet().toArray(String[]::new);
    }

    @Override
    public Collection<BeanDefinition> getBeanDefinitions() {
        return beanDefinitionMap.values();
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

    void validate(String beanName) {
        var existingBean = beanDefinitionMap.get(beanName);
        if (existingBean != null) {
            throw new BeanDefinitionOverrideException(String.format(
                    "Cannot register bean definition [%s] for bean '%s': There is already [%s] bound.",
                    existingBean, existingBean.getName(), existingBean
            ));
        }
    }
}
