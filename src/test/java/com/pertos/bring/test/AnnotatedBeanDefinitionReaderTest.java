package com.pertos.bring.test;

import com.pertos.bring.reader.BeanDefinitionReader;
import com.pertos.bring.reader.BeanDefinitionRegistry;
import com.pertos.bring.reader.impl.AnnotatedBeanDefinitionReader;
import com.pertos.bring.reader.impl.BeanDefinitionImpl;
import com.pertos.bring.reader.impl.BeanDefinitionRegistryImpl;
import com.pertos.bring.reader.impl.JavaBeanDefinitionReader;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnnotatedBeanDefinitionReaderTest {

    BeanDefinitionReader annotatedBeanDefinitionReader;
    BeanDefinitionReader javaBeanDefinitionReader;
    BeanDefinitionRegistry beanDefinitionRegistry;

    private static final String BASE_PACKAGE = "com.pertos.bring";

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
        assertThat(annotatedBeanDefinitionReader).isNotNull();
    }

    @Test
    @Order(2)
    @DisplayName("Loaded bean definitions number")
    void checkBeanDifinitionLoadNumber() {
        assertThat(annotatedBeanDefinitionReader.loadBeanDefinitions(BASE_PACKAGE)).isEqualTo(4);
    }

    @Test
    @Order(3)
    @DisplayName("Loaded bean definitions number from registry")
    void checkBeanDifinitionLoadNumberFromRegistry() {
        assertThat(beanDefinitionRegistry.getBeanDefinitionNames().length).isEqualTo(4);
    }

    @Test
    @Order(4)
    @DisplayName("ComplexService bean definition name")
    void checkComplexServiceBeanDefinitionName() {
        assertThat(beanDefinitionRegistry.getBeanDefinition("complexService").getName()).isEqualTo("complexService");
    }

    @Test
    @Order(5)
    @DisplayName("SimpleService bean definition name")
    void checkSimpleServiceBeanDefinitionName() {
        assertThat(beanDefinitionRegistry.getBeanDefinition("booblik").getName()).isEqualTo("booblik");
    }

    @Test
    @Order(6)
    @DisplayName("ConfigComplexService bean definition name")
    void checkConfigComplexServiceBeanDefinitionName() {
        assertThat(beanDefinitionRegistry.getBeanDefinition("configComplexService").getName()).isEqualTo("configComplexService");
    }

    @Test
    @Order(7)
    @DisplayName("CongigSimpleService bean definition name")
    void checkConfigSimpleServiceBeanDefinitionName() {
        assertThat(beanDefinitionRegistry.getBeanDefinition("koobik").getName()).isEqualTo("koobik");
    }

    @Test
    @Order(8)
    @DisplayName("SimpleService bean definition lazyness")
    void checkSimpleServiceBeanDefinitionLazy() {
        assertThat(beanDefinitionRegistry.getBeanDefinition("booblik").isLazy()).isEqualTo(true);
    }

    @Test
    @Order(8)
    @DisplayName("SimpleService bean definition primary")
    void checkSimpleServiceBeanDefinitionPrimary() {
        assertThat(beanDefinitionRegistry.getBeanDefinition("booblik").isPrimary()).isEqualTo(true);
    }

    @Test
    @Order(9)
    @DisplayName("Register new bean definition")
    void checkNewBeanDefinitionRegister() {
        beanDefinitionRegistry.registerBeanDefinition("newComer",
                BeanDefinitionImpl.BeanDefinitionBuilder.newInstance()
                        .withBeanClassName("className")
                        .withName("newComer")
                        .createBeanDefinitionImpl());
        assertThat(beanDefinitionRegistry.getBeanDefinition("newComer").getName()).isEqualTo("newComer");
    }

}