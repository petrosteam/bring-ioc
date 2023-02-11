package com.petros.bring.main.services.autowired.fields;

import com.petros.bring.annotations.Bean;
import com.petros.bring.annotations.Configuration;

@Configuration
public class ConfigurationBringMeToLife {
    @Bean
    public ConfigComplexService complexService() {
        return new ConfigComplexService();
    }

    @Bean(name = "koobik")
    private ConfigSimpleService simpleService() {
        return new ConfigSimpleService();
    }
}
