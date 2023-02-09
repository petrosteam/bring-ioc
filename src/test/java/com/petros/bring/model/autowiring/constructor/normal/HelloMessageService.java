package com.petros.bring.model.autowiring.constructor.normal;

import com.petros.bring.annotations.Component;

@Component
public class HelloMessageService implements MessageService {
    @Override
    public String getMessage() {
        return "Hello";
    }
}
