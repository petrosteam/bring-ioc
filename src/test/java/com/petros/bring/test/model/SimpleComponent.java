package com.petros.bring.test.model;

import com.petros.bring.annotations.Autowired;
import com.petros.bring.annotations.Component;

@Component
public class SimpleComponent {
    @Autowired
    private ComplexService complexService;

    public String getComplexServiceName() {
        return complexService.getServiceName();
    }
}
