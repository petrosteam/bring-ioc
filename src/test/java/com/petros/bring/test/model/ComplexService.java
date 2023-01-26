package com.petros.bring.test.model;

import com.petros.bring.annotations.Component;

@Component
public class ComplexService implements Service {
    @Override
    public String getServiceName() {
        return "complexService";
    }
}
