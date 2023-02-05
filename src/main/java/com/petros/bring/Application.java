package com.petros.bring;

import com.petros.bring.annotations.Order;
import com.petros.bring.bean.factory.BeanFactory;
import com.petros.bring.context.AnnotationConfigApplicationContext;
import com.petros.bring.postprocessor.OrderedBeanDefinitionPostProcessor;
import com.petros.bring.reader.BeanDefinitionReader;
import com.petros.bring.reader.BeanDefinitionRegistry;
import com.petros.bring.reader.impl.BeanDefinitionRegistryImpl;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Application {
    private static final String BRING_BASE_PACKAGE = "com.petros.bring";
    private static final Reflections innerReflections = new Reflections(BRING_BASE_PACKAGE);

    public static BeanFactory run(String packageName) {
        BeanDefinitionRegistry registry = new BeanDefinitionRegistryImpl();

        List<BeanDefinitionReader> beanDefinitionReaders = createBeanDefinitionReaders(registry);
        beanDefinitionReaders.forEach(reader -> reader.loadBeanDefinitions(packageName));

        Queue<OrderedBeanDefinitionPostProcessor> beanPostProcessors = createBeanPostProcessors();

        return new AnnotationConfigApplicationContext(registry, beanPostProcessors);
    }

    private static List<BeanDefinitionReader> createBeanDefinitionReaders(BeanDefinitionRegistry registry) {
        List<BeanDefinitionReader> result = new ArrayList<>();
        Set<Class<? extends BeanDefinitionReader>> subTypesOfBeanDefinitionReader =
                innerReflections.getSubTypesOf(BeanDefinitionReader.class);
        subTypesOfBeanDefinitionReader.forEach(subTypes -> {
            try {
                result.add(subTypes.getConstructor(BeanDefinitionRegistry.class).newInstance(registry));
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
        return result;
    }

    private static Queue<OrderedBeanDefinitionPostProcessor> createBeanPostProcessors() {
        Queue<OrderedBeanDefinitionPostProcessor> result =
                new PriorityQueue<>(Comparator.comparing(OrderedBeanDefinitionPostProcessor::getOrder));

        Set<Class<? extends OrderedBeanDefinitionPostProcessor>> subTypesOfBeanPostProcessor =
                innerReflections.getSubTypesOf(OrderedBeanDefinitionPostProcessor.class);
        subTypesOfBeanPostProcessor.forEach(subType -> {
            try {
                int order = subType.isAnnotationPresent(Order.class)
                        ? subType.getAnnotation(Order.class).value()
                        : 99;
                result.add(subType.getDeclaredConstructor(int.class).newInstance(order));
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
        return result;
    }
}
