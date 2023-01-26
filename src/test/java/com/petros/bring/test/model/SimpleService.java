package com.petros.bring.test.model;

import com.petros.bring.annotations.Component;
import com.petros.bring.annotations.Lazy;
import com.petros.bring.annotations.Primary;
import com.petros.bring.reader.Scope;

@Component(name = "booblik", scope = Scope.PROTOTYPE)
@Lazy
@Primary
public class SimpleService implements Service {

    @Override
    public String getServiceName() {
        return "simpleService";
    }
}
