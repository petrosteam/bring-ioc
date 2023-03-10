package com.petros.bring.context;

import com.petros.bring.postprocessor.BeanPostProcessor;
import com.petros.bring.reader.BeanDefinitionRegistry;
import org.junit.jupiter.api.BeforeAll;

import java.util.List;

public class AnnotationApplicationContextTest {

    private static AnnotationConfigApplicationContext applicationContext;
    private static BeanDefinitionRegistry registry;
    private static List<BeanPostProcessor> beanPostProcessors;

    @BeforeAll
    public static void setUp() {
        applicationContext = new AnnotationConfigApplicationContext(registry);
    }

}
