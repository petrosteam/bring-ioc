package com.petros.bring.main.services.qualifier;

import com.petros.bring.annotations.Component;

@Component
public class QualifierDependency {

    public String getValue() {
        return "test value";
    }
}
