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
    private static final String[] INTERNAL_PACKAGES = new String[]{
            "com.petros.bring.reader",
            "com.petros.bring.context",
            "com.petros.bring.postprocessor"
    };


    public static BeanFactory run(String packageName) {
        var context = initFactory();
        var factory = context.getBean(AnnotationConfigApplicationContext.class);
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
}
