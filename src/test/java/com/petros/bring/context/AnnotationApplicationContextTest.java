package com.petros.bring.context;

import com.petros.bring.postprocessor.OrderedBeanDefinitionPostProcessor;
import com.petros.bring.reader.BeanDefinitionRegistry;
import org.junit.jupiter.api.BeforeAll;

import java.util.Queue;

public class AnnotationApplicationContextTest {

    private static AnnotationConfigApplicationContext applicationContext;
    private static BeanDefinitionRegistry registry;
    private static Queue<OrderedBeanDefinitionPostProcessor> beanPostProcessors;

    @BeforeAll
    public static void setUp() {
        applicationContext = new AnnotationConfigApplicationContext(registry, beanPostProcessors);
    }

}
