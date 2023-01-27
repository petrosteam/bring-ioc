package com.pertos.bring.test.model;

import com.pertos.bring.annotations.Component;

@Component
public class ComplexService implements Service {
    @Override
    public String getServiceName() {
        return "complexService";
    }
}
