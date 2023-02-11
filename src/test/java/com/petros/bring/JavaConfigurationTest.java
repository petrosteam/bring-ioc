package com.petros.bring;

import com.petros.bring.bean.factory.BeanFactory;
import com.petros.bring.model.javaconfig.FtpSenderService;
import com.petros.bring.model.javaconfig.MessageService;
import com.petros.bring.model.javaconfig.SenderService;
import com.petros.bring.model.javaconfig.WriterService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JavaConfigurationTest {
    private static final String BASE_PACKAGE = "com.petros.bring.model.javaconfig";
    private static BeanFactory factory;

    @BeforeAll
    public static void init() {
        factory = Application.run(BASE_PACKAGE);
    }

    @Test
    @DisplayName("beanFactory instance exists")
    void factoryCreatedTest() {
        assertNotNull(factory);
    }

    @Test
    @DisplayName("Return simple bean created by default constructor")
    void createSimpleService() {
        MessageService messageService = factory.getBean(MessageService.class);
        assertEquals("Hello", messageService.getMessage());
    }

    @Test
    @DisplayName("Create service that has dependencies")
    void createServiceThatHasDependencies() {
        SenderService senderService = factory.getBean("httpSenderService", SenderService.class);
        assertNotNull(senderService);
        assertEquals("sending message... Hello", senderService.sendMessage());
    }

    @Test
    @DisplayName("Create service that has dependencies as method parameter")
    void createServiceThatHasDependenciesAsMethodParameter() {
        WriterService writerService = factory.getBean(WriterService.class);
        assertNotNull(writerService);
        assertEquals("writing message... Hello", writerService.writeMessage());
    }

    @Test
    @DisplayName("Create bean by @Component with @Autowired")
    void createServiceByComponent() {
        FtpSenderService ftpMessageSender = factory.getBean(FtpSenderService.class);
        assertNotNull(ftpMessageSender);
        assertEquals("sending via ftp...Hello", ftpMessageSender.sendMessage());
    }
}
