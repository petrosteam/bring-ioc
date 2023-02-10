package com.petros.bring;

import com.petros.bring.bean.factory.BeanFactory;
import com.petros.bring.context.AnnotationConfigApplicationContext;
import com.petros.bring.reader.BeanDefinitionReader;
import com.petros.bring.reader.impl.AnnotatedBeanDefinitionReader;
import com.petros.bring.reader.impl.BeanDefinitionRegistryImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Objects;


/**
 * Entry point of Bring container.
 * Static methods of this class scans beans by provided package and creates application context.
 * <p>
 * Current implementation of IoC container scans itself for components. All these components are available in
 * application context (like a bean post processors, bean factory, registry)
 */
@Slf4j
public final class Application {
    private static final String LOGO = """ 
                        
            _|_|_|    _|_|_|_|  _|_|_|_|_|  _|_|_|      _|_|      _|_|_|      _|_|_|    _|_|_|    _|_|_|  _|      _|    _|_|_| \s
            _|    _|  _|            _|      _|    _|  _|    _|  _|            _|    _|  _|    _|    _|    _|_|    _|  _|       \s
            _|_|_|    _|_|_|        _|      _|_|_|    _|    _|    _|_|        _|_|_|    _|_|_|      _|    _|  _|  _|  _|  _|_| \s
            _|        _|            _|      _|    _|  _|    _|        _|      _|    _|  _|    _|    _|    _|    _|_|  _|    _| \s
            _|        _|_|_|_|      _|      _|    _|    _|_|    _|_|_|        _|_|_|    _|    _|  _|_|_|  _|      _|    _|_|_| \s
            """;

    // There are internal bring packages are used for self-scanning
    private static final String[] INTERNAL_PACKAGES = new String[]{
            "com.petros.bring.reader",
            "com.petros.bring.context",
            "com.petros.bring.postprocessor",
            "com.petros.bring.environment"
    };


    /**
     * Run scanning package with defined loggin level
     *
     * @param packageName  package name
     * @return BeanFactory instance
     */
    public static BeanFactory run(String packageName) {
        Objects.requireNonNull(packageName, "Package should not be null");

        log.trace(LOGO);

        var context = initFactory();
        log.trace("Bring internal context has been initialized");
        var factory = context.getBean(AnnotationConfigApplicationContext.class);
        var readers = context.getAllBeans(BeanDefinitionReader.class);
        log.trace("Loading bean definitions...");
        var definitions = readers.values().stream().mapToInt(reader -> reader.loadBeanDefinitions(packageName)).sum();
        log.trace("Loaded {} bean definitions from {} ", definitions, packageName);
        factory.register();
        return context.getBean(BeanFactory.class);
    }

    /**
     * Scanning internal packages, creating a bean factory from bring-specific components
     *
     * @return bean factory
     */
    private static AnnotationConfigApplicationContext initFactory() {
        log.trace("Start scanning internal packages");
        var registry = new BeanDefinitionRegistryImpl();
        var reader = new AnnotatedBeanDefinitionReader(registry);
        log.trace("Loading internal bean definitions");
        var definitions = Arrays.stream(INTERNAL_PACKAGES).mapToInt(reader::loadBeanDefinitions).sum();
        log.trace("Loaded {} bean definitions", definitions);
        return new AnnotationConfigApplicationContext(registry);
    }
}
