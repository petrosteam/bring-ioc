package com.petros.bring.model.javaconfig;

public class HelloMessageService implements MessageService {
    @Override
    public String getMessage() {
        return "Hello";
    }
}
