package com.petros.bring.main.services.autowired.fields;

import com.petros.bring.annotations.Component;
import com.petros.bring.annotations.Configuration;

@Configuration
public class ConfigurationBringMeToLife {
    @Component
    public ConfigComplexService getComplexService() {
        return new ConfigComplexService();
    }

    @Component(name = "koobik")
    private ConfigSimpleService getSimpleService() {
        return new ConfigSimpleService();
    }
}
