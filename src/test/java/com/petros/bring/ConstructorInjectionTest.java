package com.petros.bring;

import com.petros.bring.bean.factory.BeanFactory;
import com.petros.bring.exception.CircularDependencyException;
import com.petros.bring.model.autowiring.constructor.normal.SenderService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConstructorInjectionTest {

    @Test
    void produceCircularDependency() {
        assertThrows(CircularDependencyException.class,
                () -> Application.run("com.petros.bring.model.autowiring.constructor.circular"));
    }

    @Test
    void constructorInjection() {
        BeanFactory factory = Application.run("com.petros.bring.model.autowiring.constructor.normal");
        assertNotNull(factory);
        SenderService senderService = factory.getBean(SenderService.class);
        assertEquals("Sending via ftp... Hello", senderService.sendMessage());
    }
}
