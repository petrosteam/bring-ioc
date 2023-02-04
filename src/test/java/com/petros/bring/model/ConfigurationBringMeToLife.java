package com.petros.bring.model;

import com.petros.bring.annotations.Component;
import com.petros.bring.annotations.Configuration;
import com.petros.bring.reader.Scope;

@Configuration
public class ConfigurationBringMeToLife {
    @Component
    public ConfigComplexService getComplexService() {
        return new ConfigComplexService();
    }

    @Component(name = "koobik", scope = Scope.PROTOTYPE)
    private ConfigSimpleService getSimpleService() {
        return null;
    }
}
