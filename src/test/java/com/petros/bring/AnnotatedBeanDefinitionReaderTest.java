package com.petros.bring;

import com.petros.bring.reader.BeanDefinitionReader;
import com.petros.bring.reader.BeanDefinitionRegistry;
import com.petros.bring.reader.impl.AnnotatedBeanDefinitionReader;
import com.petros.bring.reader.impl.BeanDefinitionImpl;
import com.petros.bring.reader.impl.BeanDefinitionRegistryImpl;
import com.petros.bring.reader.impl.JavaBeanDefinitionReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnnotatedBeanDefinitionReaderTest {

    BeanDefinitionReader annotatedBeanDefinitionReader;
    BeanDefinitionReader javaBeanDefinitionReader;
    BeanDefinitionRegistry beanDefinitionRegistry;

    private static final String BASE_PACKAGE = "com.petros.bring.main.services";

    @BeforeEach
    public void init() {
        beanDefinitionRegistry = new BeanDefinitionRegistryImpl();
        annotatedBeanDefinitionReader = new AnnotatedBeanDefinitionReader(beanDefinitionRegistry);
        annotatedBeanDefinitionReader.loadBeanDefinitions(BASE_PACKAGE);
        javaBeanDefinitionReader = new JavaBeanDefinitionReader(beanDefinitionRegistry);
        javaBeanDefinitionReader.loadBeanDefinitions(BASE_PACKAGE);
    }

    @Test
    @Order(1)
    @DisplayName("AnnotatedBeanDefinitionReader instance exists")
    void annotatedBeanDefinitionReaderClassExists() {
        assertNotNull(annotatedBeanDefinitionReader);
    }

    @Test
    @Order(2)
    @DisplayName("ComplexService bean definition name")
    void checkComplexServiceBeanDefinitionName() {
        assertEquals(beanDefinitionRegistry.getBeanDefinition("complexServiceWithSimpleService").getName(), "complexServiceWithSimpleService");
    }

    @Test
    @Order(3)
    @DisplayName("SimpleService bean definition name")
    void checkSimpleServiceBeanDefinitionName() {
        assertEquals(beanDefinitionRegistry.getBeanDefinition("booblik").getName(), "booblik");
    }

    @Test
    @Order(4)
    @DisplayName("ConfigComplexService bean definition name")
    void checkConfigComplexServiceBeanDefinitionName() {
        assertEquals(beanDefinitionRegistry.getBeanDefinition("configComplexService").getName(), "configComplexService");
    }

    @Test
    @Order(5)
    @DisplayName("CongigSimpleService bean definition name")
    void checkConfigSimpleServiceBeanDefinitionName() {
        assertEquals(beanDefinitionRegistry.getBeanDefinition("koobik").getName(), "koobik");
    }

    @Test
    @Order(6)
    @DisplayName("SimpleService bean definition lazyness")
    void checkSimpleServiceBeanDefinitionLazy() {
        assertTrue(beanDefinitionRegistry.getBeanDefinition("booblik").isLazy());
    }

    @Test
    @Order(7)
    @DisplayName("SimpleService bean definition primary")
    void checkSimpleServiceBeanDefinitionPrimary() {
        assertTrue(beanDefinitionRegistry.getBeanDefinition("booblik").isPrimary());
    }

    @Test
    @Order(8)
    @DisplayName("Register new bean definition")
    void checkNewBeanDefinitionRegister() {
        beanDefinitionRegistry.registerBeanDefinition("newComer".getClass(),
                BeanDefinitionImpl.builder()
                        .beanClassName("className")
                        .name("newComer")
                        .build());
         assertEquals(beanDefinitionRegistry.getBeanDefinition("newComer").getName(), "newComer");

    }

    @Test
    @Order(9)
    @DisplayName("Check add constructor value to depends on")
    void checkAddOneConstructorToDependsOnThanSuccess() {
        var testUserNoDefaultConstructor =
                annotatedBeanDefinitionReader.getBeanDefinitionRegistry().getBeanDefinition("testUserNoDefaultConstructor");
        assertNotNull(testUserNoDefaultConstructor.getDependsOn());
    }

    @Test
    @Order(10)
    @DisplayName("Check add constructor value to depends on")
    void checkAddOneConstructorToDependsOnThanError() {
        var testUserNoDefaultConstructor =
                annotatedBeanDefinitionReader.getBeanDefinitionRegistry().getBeanDefinition("testIdClass");
        assertNull(testUserNoDefaultConstructor.getDependsOn());
    }
}
