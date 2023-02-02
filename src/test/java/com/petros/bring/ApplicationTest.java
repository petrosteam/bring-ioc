package com.petros.bring;

import com.petros.bring.bean.factory.BeanFactory;
import com.petros.bring.test.model.ComplexService;
import com.petros.bring.test.model.ComplexService;
import com.petros.bring.test.model.Service;
import com.petros.bring.test.model.SimpleService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import com.petros.bring.test.model.SimpleComponent;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApplicationTest {
    private static final String BASE_PACKAGE = "com.petros.bring";
    private static BeanFactory factory;

    @BeforeAll
    public static void init() {
        factory = Application.run(BASE_PACKAGE);
    }

    @Test
    void run_whenMoreThanOneMatchingBean_thenReturnPrimaryOne() {
        BeanFactory factory = Application.run("com.petros.bring.test.model");
        assertNotNull(factory);
        System.out.println(factory);
        Service service = factory.getBean(Service.class);
        assertEquals("simpleService", service.getServiceName());
        SimpleComponent simpleComponent = factory.getBean(SimpleComponent.class);
        System.out.println(simpleComponent.getComplexServiceName());
    }

    @Test
    void run_run_whenMoreThanOneMatchingBean_thenAutowirePrimaryOne() {
        assertThat(factory).isNotNull();
        ComplexService complexService = factory.getBean(ComplexService.class);
        SimpleService simpleService = (SimpleService) factory.getBean(Service.class);
        assertThat(complexService.getService()).isSameAs(simpleService);
    }


//    @Test
//    void run_whenOneMatchingBean_thenReturnIt() {
//        BeanFactory factory = Application.run("com.petros.bring");
//        assertNotNull(factory);
//        Service service = factory.getBean(ConfigSimpleService.class);
//        assertEquals("configSimpleService", service.getServiceName());
//    }
}