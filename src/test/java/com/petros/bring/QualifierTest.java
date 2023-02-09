package com.petros.bring;

import com.petros.bring.bean.factory.BeanFactory;
import com.petros.bring.main.services.qualifier.BeanWithQualifier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class QualifierTest {

    private static final String BASE_PACKAGE = "com.petros.bring.main";
    private static BeanFactory factory;

    @BeforeAll
    public static void init() {
        factory = Application.run(BASE_PACKAGE);
    }

    @Test
    @DisplayName("check services with field autowired by class cycled")
    void getServicesWithFieldAutowiredByClassCycled() {
        BeanWithQualifier beanQualifier = factory.getBean(BeanWithQualifier.class);
        assertNotNull(beanQualifier.getDependency());
        assertEquals("test value", beanQualifier.getDependency().getValue());
    }
}
