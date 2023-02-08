package com.petros.bring.model.autowiring.constructor.circular;

import com.petros.bring.annotations.Component;

@Component
public class CDependsOnA implements C {
    private final A a;

    public CDependsOnA(A a) {
        this.a = a;
    }
}
