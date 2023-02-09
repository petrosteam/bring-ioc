package com.petros.bring.model.autowiring.constructor.circular;

import com.petros.bring.annotations.Component;

@Component
public class BDependsOnC implements B {
    private final C c;

    public BDependsOnC(C c) {
        this.c = c;
    }
}
