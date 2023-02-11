package com.petros.bring.model.javaconfig;

import com.petros.bring.annotations.Bean;
import com.petros.bring.annotations.Configuration;

@Configuration
public class JavaConfiguration {

    @Bean
    public MessageService helloMessageService() {
        return new HelloMessageService();
    }

    @Bean
    public SenderService httpSenderService() {
        return new HttpSenderService(helloMessageService());
    }

    @Bean
    public WriterService writerService(MessageService messageService) {
        return new HelloWriterService(messageService);
    }
}
