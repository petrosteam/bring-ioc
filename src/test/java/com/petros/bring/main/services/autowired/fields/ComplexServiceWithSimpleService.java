package com.petros.bring.main.services.autowired.fields;

import com.petros.bring.annotations.Autowired;
import com.petros.bring.annotations.Component;
import com.petros.bring.main.services.Service;

@Component
public class ComplexServiceWithSimpleService implements Service {
    @Autowired
    private SimplePrimaryService service;


    public Service getService() {
        return service;
    }
}
