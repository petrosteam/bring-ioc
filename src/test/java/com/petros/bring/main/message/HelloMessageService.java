package com.petros.bring.main.message;

import com.petros.bring.annotations.Component;

@Component
public class HelloMessageService implements MessageService {
    @Override
    public String getMessage() {
        return "Hello";
    }
}
