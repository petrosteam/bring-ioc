package com.petros.bring.value;

import com.petros.bring.Application;
import com.petros.bring.bean.factory.BeanFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FailValueFieldInjectionNumberFormatTest {

    private static final String BASE_PACKAGE = "com.petros.bring.value.context.numberformat";
    private static BeanFactory factory;

    @Test
    @DisplayName("Simple service with injected String value with @Value")
    void fieldValueInjectionFailsWithNumberFormatException() {
        assertThrows(NumberFormatException.class, () -> Application.run(BASE_PACKAGE));
    }
}
