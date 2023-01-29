package com.pertos.bring.context;

import com.pertos.bring.exception.NoSuchBeanException;
import com.pertos.bring.exception.NoUniqueBeanException;
import com.pertos.bring.postprocessor.BeanPostProcessor;
import com.pertos.bring.reader.BeanDefinition;
import com.pertos.bring.reader.BeanDefinitionRegistry;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AnnotationConfigApplicationContext implements ApplicationContext {

    BeanDefinitionRegistry registry;
    List<BeanPostProcessor> postProcessors;
    Map<Class<?>, Object> context = new ConcurrentHashMap<>();

    @Override
    public <T> T getBean(Class<T> beanType) throws NoSuchBeanException, NoUniqueBeanException {
        if (context.containsKey(beanType)) {
            return (T) context.get(beanType);
        }
        BeanDefinition beanDefinition = registry.getBeanDefinition(beanType.getSimpleName());
        Object bean = createBean(beanType, beanDefinition);
        return (T) bean;
    }

    @Override
    public <T> T getBean(String name, Class<T> beanType) throws NoSuchBeanException, NoUniqueBeanException {
        return null;
    }

    @Override
    public <T> Map<String, T> getAllBeans(Class<T> beanType) {
        return null;
    }

    public AnnotationConfigApplicationContext(BeanDefinitionRegistry registry, List<BeanPostProcessor> beanPostProcessors) {
        this.registry = registry;
        this.postProcessors = beanPostProcessors;
    }

    private Object createBean(Class<?> aClass, BeanDefinition beanDefinition) {
        try {
            Object obj = null;
            if (beanDefinition.getDependsOn() != null) {
                Map<String, Object> beansByName = new HashMap<>();
                for (String dependsOnName : beanDefinition.getDependsOn()) {
                    Class<?> bClass = Class.forName(beanDefinition.getBeanClassName());
                    beansByName.put(dependsOnName, getBean(bClass));
                }
                // check for circular dependencies
//                obj = aClass.getConstructor().newInstance() with non default constructor
            } else {
                obj = aClass.getConstructor().newInstance();
            }
            for (BeanPostProcessor postProcessor : postProcessors) {
                postProcessor.postProcessBeforeInitialization(aClass, obj);
                postProcessor.postProcessAfterInitialization(aClass, obj);
            }
            return obj;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
