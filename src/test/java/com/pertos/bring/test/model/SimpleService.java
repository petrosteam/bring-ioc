package com.pertos.bring.test.model;

import com.pertos.bring.annotations.Component;
import com.pertos.bring.annotations.Lazy;
import com.pertos.bring.annotations.Primary;
import com.pertos.bring.reader.Scope;

@Component(name = "booblik", scope = Scope.PROTOTYPE)
@Lazy
@Primary
public class SimpleService implements Service {

    @Override
    public String getServiceName() {
        return "simpleService";
    }
}
