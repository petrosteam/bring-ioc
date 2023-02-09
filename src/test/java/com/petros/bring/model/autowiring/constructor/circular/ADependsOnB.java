package com.petros.bring.model.autowiring.constructor.circular;

import com.petros.bring.annotations.Component;

@Component
public class ADependsOnB implements A {
    private final B b;

    public ADependsOnB(B b) {
        this.b = b;
    }
}
