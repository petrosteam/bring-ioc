package com.petros.bring.bean.factory;

import com.petros.bring.exception.NoSuchBeanException;
import com.petros.bring.exception.NoUniqueBeanException;
import com.petros.bring.reader.BeanDefinitionRegistry;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class AnnotationBeanFactory implements BeanFactory {

    protected final BeanDefinitionRegistry registry;
    protected static final Map<String, Object> rootContextMap = new ConcurrentHashMap<>();

    public AnnotationBeanFactory(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    @Override
    public <T> T getBean(Class<T> beanType) throws NoSuchBeanException, NoUniqueBeanException {
        Map<String, T> matchingBeans = getAllBeans(beanType);
        if (matchingBeans.size() > 1) {
            return getPrimary(beanType, matchingBeans);
        }
        return matchingBeans.values().stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchBeanException("No bean available"));
    }

    private <T> T getPrimary(Class<T> beanType, Map<String, T> matchingBeans) {
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
