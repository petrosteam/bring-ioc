package com.petros.bring.model;

import com.petros.bring.annotations.Component;
import com.petros.bring.annotations.Lazy;
import com.petros.bring.annotations.Primary;

@Component(name = "booblik")
@Lazy
@Primary
public class SimpleService implements Service {

    @Override
    public String getServiceName() {
        return "simpleService";
    }
}
