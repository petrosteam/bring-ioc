package com.petros.bring.bean.factory;

import com.petros.bring.exception.NoSuchBeanException;
import com.petros.bring.exception.NoUniqueBeanException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class AnnotationBeanFactory implements BeanFactory {

    protected static final Map<String, Object> rootContextMap = new ConcurrentHashMap<>();

    @Override
    public <T> T getBean(Class<T> beanType) throws NoSuchBeanException, NoUniqueBeanException {
        Map<String, T> matchingBeans = getAllBeans(beanType);
        if (matchingBeans.size() > 1) {
            throw new NoUniqueBeanException(beanType + " expecting matching bean but not found");
        }
        return matchingBeans.values().stream()
                .findAny()
                .orElseThrow(() -> new NoSuchBeanException("No bean available"));
    }

    @Override
    public <T> T getBean(String name, Class<T> beanType) throws NoSuchBeanException, NoUniqueBeanException {
        return rootContextMap.entrySet().stream()
                .filter(beanEntry -> name.equals(beanEntry.getKey()))
                .findAny()
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
