package com.pertos.bring.test.model;

import com.pertos.bring.annotations.Component;
import com.pertos.bring.annotations.Configuration;
import com.pertos.bring.reader.Scope;

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
