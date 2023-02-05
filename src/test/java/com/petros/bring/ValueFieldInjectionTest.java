package com.petros.bring;

import com.petros.bring.bean.factory.BeanFactory;
import com.petros.bring.main.services.autowired.fields.ServiceWithInjectedFieldValues;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValueFieldInjectionTest {

    private static final String BASE_PACKAGE = "com.petros.bring";
    private static BeanFactory factory;

    @BeforeAll
    public static void init() {
        factory = Application.run(BASE_PACKAGE);
    }

    @Test
    @DisplayName("Simple service with injected String value with @Value")
    void fieldValueInjectionWithoutApplicationPropertiesTest() {
        ServiceWithInjectedFieldValues service = factory.getBean(ServiceWithInjectedFieldValues.class);
        assertEquals("stringValue1", service.getDefaultStringValue());
        assertEquals(1, service.getDefaultIntValue());
        assertEquals(2.0, service.getDefaultDoubleValue());
        assertEquals(3L, service.getDefaultLongValue());
        assertEquals(4, service.getDefaultShortValue());
        assertEquals(5, service.getDefaultByteValue());
        assertEquals("defaultValueInsteadOfProperties", service.getAbsentValueFromApplicationProperties());
    }
}
