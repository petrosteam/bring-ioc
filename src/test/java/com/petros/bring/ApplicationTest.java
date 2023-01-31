package com.petros.bring;

import com.petros.bring.bean.factory.BeanFactory;
import com.petros.bring.test.model.Service;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {

    @Test
    void run_whenMoreThanOneMatchingBean_thenReturnPrimaryOne() {
        BeanFactory context = Application.run("com.petros.bring");
        assertNotNull(context);
        Service service = context.getBean(Service.class);
        assertEquals("simpleService", service.getServiceName());
    }
}