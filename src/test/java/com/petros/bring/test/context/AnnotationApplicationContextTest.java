package com.petros.bring.test.context;

import com.petros.bring.context.AnnotationConfigApplicationContext;
import com.petros.bring.reader.BeanDefinitionRegistry;
import org.junit.jupiter.api.BeforeAll;

public class AnnotationApplicationContextTest {

    private static AnnotationConfigApplicationContext applicationContext;
    private static BeanDefinitionRegistry registry;

    @BeforeAll
    public static void setUp() {
        applicationContext = new AnnotationConfigApplicationContext(registry);
    }

}
