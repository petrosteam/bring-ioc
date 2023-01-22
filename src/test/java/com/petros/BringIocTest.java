package com.petros;

import com.petros.bring.ApplicationContext;
import com.petros.client.services.ServiceWithDependency;
import com.petros.client.services.ServiceSimple;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BringIocTest {

    private static final String BRING_IOC_PACKAGE = "com.petros.bring";
    private static final String APP_CONTEXT_CLASS = "ApplicationContext";

    private ApplicationContext applicationContext;

    @BeforeEach
    void init(){
        applicationContext = new ApplicationContext("com.petros.client");
    }

    @Test
    @Order(1)
    @DisplayName("BringIocTest class exists")
    void appContextClassExists() throws ClassNotFoundException {
        Class.forName(BRING_IOC_PACKAGE + "." + APP_CONTEXT_CLASS);
    }




    @Test
    @Order(2)
    @DisplayName("Service class instantinated")
    void serviceSimpleInstantinate() {
        String serviceSimple = applicationContext.getBean(ServiceSimple.class).getServiceName();
        assert serviceSimple.equals("s2");
    }


    @Test
    @Order(3)
    @DisplayName("Service class instantinated")
    void serviceWithDepencyInstantinate() {
        String serviceWithDepency = applicationContext.getBean(ServiceWithDependency.class).getServiceName();
        assert serviceWithDepency.equals("s1+s2");
    }



}
