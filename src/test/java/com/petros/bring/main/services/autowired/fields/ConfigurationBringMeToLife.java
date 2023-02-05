package com.petros.bring.main.services.autowired.fields;

import com.petros.bring.annotations.Bean;
import com.petros.bring.annotations.Configuration;

@Configuration
public class ConfigurationBringMeToLife {
    @Bean
    public ConfigComplexService getComplexService() {
        return new ConfigComplexService();
    }

    @Bean
    private ConfigSimpleService getSimpleService() {
        return new ConfigSimpleService();
    }
}
