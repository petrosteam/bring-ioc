package com.petros.bring;

import com.petros.bring.bean.factory.BeanFactory;
import com.petros.bring.context.AnnotationConfigApplicationContext;
import com.petros.bring.environment.ApplicationEnvironment;
import com.petros.bring.environment.PropertyResolver;
import com.petros.bring.environment.convert.TypeConversionService;
import com.petros.bring.environment.convert.TypeConverter;
import com.petros.bring.exception.ApplicationEnvironmentException;
import com.petros.bring.exception.RunApplicationContextException;
import com.petros.bring.reader.BeanDefinitionReader;
import com.petros.bring.reader.BeanDefinitionRegistry;
import com.petros.bring.reader.impl.AnnotatedBeanDefinitionReader;
import com.petros.bring.reader.impl.BeanDefinitionRegistryImpl;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Application {
    private static final String BRING_BASE_PACKAGE = "com.petros.bring";
    private static final Reflections innerReflections = new Reflections(BRING_BASE_PACKAGE);

    private static final String[] INTERNAL_PACKAGES = new String[]{
            "com.petros.bring.reader",
            "com.petros.bring.context",
            "com.petros.bring.postprocessor",
            "com.petros.bring.environment"
    };


    public static BeanFactory run(String packageName) {
        var context = initFactory();
        var factory = context.getBean(AnnotationConfigApplicationContext.class);
        var readers = context.getAllBeans(BeanDefinitionReader.class);
        readers.values().forEach(reader -> reader.loadBeanDefinitions(packageName));
        factory.register();
        return context.getBean(BeanFactory.class);
    }

    private static AnnotationConfigApplicationContext initFactory() {
        var registry = new BeanDefinitionRegistryImpl();
        var reader = new AnnotatedBeanDefinitionReader(registry);
        Arrays.stream(INTERNAL_PACKAGES).forEach(reader::loadBeanDefinitions);
        return new AnnotationConfigApplicationContext(registry);
    }
}
