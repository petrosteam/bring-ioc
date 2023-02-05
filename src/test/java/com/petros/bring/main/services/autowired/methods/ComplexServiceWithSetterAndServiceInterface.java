package com.petros.bring.main.services.autowired.methods;

import com.petros.bring.annotations.Autowired;
import com.petros.bring.annotations.Component;
import com.petros.bring.main.services.Service;

@Component
public class ComplexServiceWithSetterAndServiceInterface implements Service {

    private Service service;

    @Autowired
    public void setService(Service service) {
        this.service = service;
    }

    public Service getService() {
        return service;
    }
}
