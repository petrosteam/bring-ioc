package com.petros.bring;

import com.petros.bring.bean.factory.BeanFactory;
import com.petros.bring.main.services.autowired.methods.ComplexServiceWithSetterAndSimpleService;
import com.petros.bring.main.services.autowired.methods.SimpleServiceWithSetter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AutowiredSettersTest {

    private static final String BASE_PACKAGE = "com.petros.bring";
    private static BeanFactory factory;

    @BeforeAll
    public static void init() {
        factory = Application.run(BASE_PACKAGE);
    }

    @Test
    @DisplayName("beanFactory instance exists")
    void factoryCreatedTest() {
        assertNotNull(factory);
    }

    @Test
    @DisplayName("simple service with setter exists")
    void getSimpleServiceTest() {
       assertEquals(factory.getBean(SimpleServiceWithSetter.class).getClass(), SimpleServiceWithSetter.class);
    }

    @Test
    @DisplayName("check complex service with setter autowired by class")
    void getComplesServiceWithFieldAutowiredByClass() {
        ComplexServiceWithSetterAndSimpleService complexServiceWithSimpleService = factory.getBean(ComplexServiceWithSetterAndSimpleService.class);
        SimpleServiceWithSetter simpleService = factory.getBean(SimpleServiceWithSetter.class);
        assertEquals(complexServiceWithSimpleService.getService(), simpleService);
    }


}
