package com.petros.bring;

import com.petros.bring.bean.factory.BeanFactory;
import com.petros.bring.context.AnnotationConfigApplicationContext;
import com.petros.bring.postprocessor.BeanPostProcessor;
import com.petros.bring.reader.BeanDefinitionReader;
import com.petros.bring.reader.BeanDefinitionRegistry;
import com.petros.bring.reader.impl.BeanDefinitionRegistryImpl;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Application {
    private static final String BRING_BASE_PACKAGE = "com.petros.bring";
    private static final Reflections innerReflections = new Reflections(BRING_BASE_PACKAGE);

    public static BeanFactory run(String packageName) {
        BeanDefinitionRegistry registry = new BeanDefinitionRegistryImpl();

        List<BeanDefinitionReader> beanDefinitionReaders = createBeanDefinitionReaders(registry);
        beanDefinitionReaders.forEach(reader -> reader.loadBeanDefinitions(packageName));

        List<BeanPostProcessor> beanPostProcessors = createBeanPostProcessors();

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

    private static List<BeanPostProcessor> createBeanPostProcessors() {
        List<BeanPostProcessor> result = new ArrayList<>();
        Set<Class<? extends BeanPostProcessor>> subTypesOfBeanPostProcessor =
                innerReflections.getSubTypesOf(BeanPostProcessor.class);
        subTypesOfBeanPostProcessor.forEach(subTypes -> {
            try {
                result.add(subTypes.getConstructor().newInstance());
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
