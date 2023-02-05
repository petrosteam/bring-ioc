package com.petros.bring.test.model;

import com.petros.bring.annotations.Autowired;
import com.petros.bring.annotations.Component;
import com.petros.bring.model.ComplexService;

@Component
public class SimpleComponent {
    @Autowired
    private ComplexService complexService;

    public String getComplexServiceName() {
        return complexService.getServiceName();
    }
}
