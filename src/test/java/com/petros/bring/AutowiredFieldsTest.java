package com.petros.bring;

import com.petros.bring.bean.factory.BeanFactory;
import com.petros.bring.main.services.autowired.fields.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AutowiredFieldsTest {

    private static final String BASE_PACKAGE = "com.petros.bring.main";
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
    @DisplayName("simple service exists")
    void getSimpleServiceTest() {
       assertEquals(factory.getBean(SimplePrimaryService.class).getClass(), SimplePrimaryService.class);
    }

    @Test
    @DisplayName("check complex service with field autowired by class")
    void getComplesServiceWithFieldAutowiredByClass() {
        ComplexServiceWithSimpleService complexServiceWithSimpleService = factory.getBean(ComplexServiceWithSimpleService.class);
        SimplePrimaryService simpleService = factory.getBean(SimplePrimaryService.class);
        assertEquals(complexServiceWithSimpleService.getClass(), ComplexServiceWithSimpleService.class);
        assertEquals(complexServiceWithSimpleService.getService(), simpleService);
    }

    @Test
    @DisplayName("check services with field autowired by class cycled")
    void getServicesWithFieldAutowiredByClassCycled() {
        ServiceForCycleA serviceForCycleA = factory.getBean(ServiceForCycleA.class);
        ServiceForCycleB serviceForCycleB = factory.getBean(ServiceForCycleB.class);
        assertEquals(serviceForCycleA, serviceForCycleB.getService());
        assertEquals(serviceForCycleB, serviceForCycleA.getService());
    }

    @Test
    @DisplayName("check service with field autowired by interface")
    void getServicesWithFieldAutowiredByInterface() {
        ServiceWithFieldWiredByInterface serviceWithFieldWiredByInterface = factory.getBean(ServiceWithFieldWiredByInterface.class);
        SimplePrimaryService simplePrimaryService = factory.getBean(SimplePrimaryService.class);
        assertEquals(serviceWithFieldWiredByInterface.getService(), simplePrimaryService);
    }

}
