package com.petros.bring.main.services.autowired.fields;

import com.petros.bring.annotations.Autowired;
import com.petros.bring.annotations.Component;
import com.petros.bring.main.services.Service;

@Component
public class ServiceForCycleB implements Service {
    @Autowired
    private ServiceForCycleA service;

    public Service getService() {
        return service;
    }
}
