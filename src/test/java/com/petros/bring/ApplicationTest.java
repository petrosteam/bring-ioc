package com.petros.bring;

import com.petros.bring.bean.factory.BeanFactory;
import com.petros.bring.exception.NoUniqueBeanException;
import com.petros.bring.main.image.ImageService;
import com.petros.bring.main.image.MarsImageService;
import com.petros.bring.main.image.MoonImageService;
import com.petros.bring.main.message.HelloMessageService;
import com.petros.bring.main.message.MessageService;
import com.petros.bring.main.prototype.AudiCasService;
import com.petros.bring.main.prototype.CarService;
import com.petros.bring.main.prototype.KiaCarService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import com.petros.bring.test.model.SimpleComponent;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {
    private static final String BASE_PACKAGE = "com.petros.bring";
    private static BeanFactory factory;

    @BeforeAll
    public static void init() {
        factory = Application.run(BASE_PACKAGE);
    }

    @Test
    @DisplayName("BeanFactory instance exists")
    void factoryCreatedTest() {
        assertNotNull(factory);
    }

    @Test
    @DisplayName("Return single matching bean by interface")
    void getSingleBeanByInterface() {
        MessageService serviceByInterface = factory.getBean(MessageService.class);
        assertEquals("Hello", serviceByInterface.getMessage());
    }

    @Test
    @DisplayName("Return single matching bean by class")
    void getSingleBeanByClass() {
        MessageService serviceByClass = factory.getBean(HelloMessageService.class);
        assertEquals("Hello", serviceByClass.getMessage());
    }

    @Test
    @DisplayName("Return the same single matching bean, no matter it was called by interface or class")
    void getTheSameSingleBeanByInterfaceAndClass() {
        MessageService byClass = factory.getBean(HelloMessageService.class);
        MessageService byInterface = factory.getBean(MessageService.class);
        assertEquals(byClass, byInterface);
    }

    @Test
    @DisplayName("Throw exception when non unique bean called by interface")
    void throwNoUniqueBeanException() {
        assertThrows(NoUniqueBeanException.class, () -> factory.getBean(ImageService.class));
    }

    @Test
    @DisplayName("Return two different beans by classes that implement the same interface")
    void getTwoDifferentBeansImplementedTheSameInterface() {
        ImageService marsImageService = factory.getBean(MarsImageService.class);
        ImageService moonImageService = factory.getBean(MoonImageService.class);
        assertNotEquals(marsImageService, moonImageService);
        assertEquals("mars image", marsImageService.getImage());
        assertEquals("moon image", moonImageService.getImage());
    }

    @Test
    @DisplayName("Return singleton if bean called by interface and more that one implementation exists. " +
            "One implementation - singleton, other - prototypes")
    void getSingletonFromNonUniqueImplementations() {
        CarService carService = factory.getBean(CarService.class);
        assertEquals("kia", carService.getCar());
    }

    @Test
    @DisplayName("Return the different objects when prototype object was called")
    void getPrototypeTwice() {
        CarService audiCasService1 = factory.getBean(AudiCasService.class);
        CarService audiCasService2 = factory.getBean(AudiCasService.class);
        assertNotEquals(audiCasService1, audiCasService2);
    }

    @Test
    @DisplayName("Return the same object when singleton object was called")
    void getSingletonTwice() {
        CarService kiaCarService1 = factory.getBean(KiaCarService.class);
        CarService kiaCarService2 = factory.getBean(KiaCarService.class);
        assertEquals(kiaCarService1, kiaCarService2);
    }
}