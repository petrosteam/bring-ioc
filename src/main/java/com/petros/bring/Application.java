package com.petros.bring;

import com.petros.bring.bean.factory.BeanFactory;
import com.petros.bring.context.AnnotationConfigApplicationContext;
import com.petros.bring.reader.BeanDefinitionReader;
import com.petros.bring.reader.BeanDefinitionRegistry;
import com.petros.bring.reader.impl.AnnotatedBeanDefinitionReader;
import com.petros.bring.reader.impl.BeanDefinitionRegistryImpl;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Application {
    private static final String BRING_BASE_PACKAGE = "com.petros.bring";
    private static final Reflections innerReflections = new Reflections(BRING_BASE_PACKAGE);

    private static final String[] INTERNAL_PACKAGES = new String[]{
            "com.petros.bring.reader",
            "com.petros.bring.bean",
            "com.petros.bring.context",
            "com.petros.bring.postprocessor"
    };

    private static final String[] MIDDLEWARE = new String[]{
            "com.petros.bring.postprocessor"
    };

    public static BeanFactory run(String packageName) {
        var factory = initFactory();
        var registry = factory.getBean(BeanDefinitionRegistry.class);
        var readers = factory.getAllBeans(BeanDefinitionReader.class);
        readers.values().forEach(reader -> reader.loadBeanDefinitions(packageName));
        factory.getBean(AnnotationConfigApplicationContext.class).register(registry);
        return factory.getBean(AnnotationConfigApplicationContext.class);
    }

    private static AnnotationConfigApplicationContext initFactory() {
        var registry = new BeanDefinitionRegistryImpl();
        var reader = new AnnotatedBeanDefinitionReader(registry);
        Arrays.stream(INTERNAL_PACKAGES).forEach(reader::loadBeanDefinitions);
        return new AnnotationConfigApplicationContext(registry);
    }

//    private static List<BeanDefinitionReader> createBeanDefinitionReaders(BeanDefinitionRegistry registry) {
//        List<BeanDefinitionReader> result = new ArrayList<>();
//        Set<Class<? extends BeanDefinitionReader>> subTypesOfBeanDefinitionReader =
//                innerReflections.getSubTypesOf(BeanDefinitionReader.class);
//        subTypesOfBeanDefinitionReader.forEach(subTypes -> {
//            try {
//                result.add(subTypes.getConstructor(BeanDefinitionRegistry.class).newInstance(registry));
//            } catch (InstantiationException e) {
//                throw new RuntimeException(e);
//            } catch (IllegalAccessException e) {
//                throw new RuntimeException(e);
//            } catch (InvocationTargetException e) {
//                throw new RuntimeException(e);
//            } catch (NoSuchMethodException e) {
//                throw new RuntimeException(e);
//            }
//        });
//        return result;
//    }result
}
