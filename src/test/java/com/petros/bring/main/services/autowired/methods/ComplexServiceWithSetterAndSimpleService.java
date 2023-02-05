package com.petros.bring.main.services.autowired.methods;

import com.petros.bring.annotations.Autowired;
import com.petros.bring.annotations.Component;
import com.petros.bring.main.services.Service;

@Component
public class ComplexServiceWithSetterAndSimpleService implements Service {

    private SimpleServiceWithSetter service;

    @Autowired
    public void setService(SimpleServiceWithSetter service) {
        this.service = service;
    }

    public Service getService() {
        return service;
    }
}
