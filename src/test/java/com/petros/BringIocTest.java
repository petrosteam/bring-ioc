package com.petros;

import com.petros.bring.ApplicationContext;
import com.petros.client.services.Service1;
import com.petros.client.services.Service2;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BringIocTest {

    private static final String BRING_IOC_PACKAGE = "com.petros.bring";
    private static final String APP_CONTEXT_CLASS = "ApplicationContext";

    @Test
    @Order(1)
    @DisplayName("BringIocTest class exists")
    void appContextClassExists() throws ClassNotFoundException {
        Class.forName(BRING_IOC_PACKAGE + "." + APP_CONTEXT_CLASS);
    }

    @Test
    @Order(2)
    @DisplayName("Service class instantinated")
    void service1Instantinate() {
        ApplicationContext applicationContext = new ApplicationContext("com.petros.client");
        String serviceName1 = applicationContext.getBean(Service1.class).getServiceName();
        assert serviceName1.equals("s1+s2");
    }

    @Test
    @Order(2)
    @DisplayName("Service class instantinated")
    void service2Instantinate() {
        ApplicationContext applicationContext = new ApplicationContext("com.petros.client");
        String serviceName2 = applicationContext.getBean(Service2.class).getServiceName();
        assert serviceName2.equals("s2");
    }

}
