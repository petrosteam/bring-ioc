package com.petros.bring.model.configuration;

public class ADependsOnB implements A {
    private final B b;

    public ADependsOnB(B b) {
        this.b = b;
    }
}
