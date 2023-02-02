package com.petros.bring.model;

import com.petros.bring.annotations.Autowired;
import com.petros.bring.annotations.Component;

@Component
public class ComplexService implements Service {
    @Autowired
    private Service service;

    @Override
    public String getServiceName() {
        return "complexService";
    }

    public Service getService() {
        return service;
    }

    @Override
    public String toString() {
        return "ComplexService{" +
                "simpleService=" + service +
                '}';
    }
}
