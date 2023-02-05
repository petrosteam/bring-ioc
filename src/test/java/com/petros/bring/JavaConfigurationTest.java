package com.petros.bring;

import com.petros.bring.bean.factory.BeanFactory;
import com.petros.bring.model.configuration.A;
import org.junit.jupiter.api.Test;

public class JavaConfigurationTest {

    @Test
    void test() {
        BeanFactory factory = Application.run("com.petros.bring.model.configuration");
        factory.getBean(A.class);
    }
}
