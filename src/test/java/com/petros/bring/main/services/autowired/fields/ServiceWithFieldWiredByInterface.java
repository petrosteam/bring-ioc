package com.petros.bring.main.services.autowired.fields;

import com.petros.bring.annotations.Autowired;
import com.petros.bring.annotations.Component;
import com.petros.bring.main.services.Service;

@Component
public class ServiceWithFieldWiredByInterface implements Service {

    @Autowired
    Service service;

    public Service getService() {
        return service;
    }
}
