package com.petros.bring.model.javaconfig;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HelloWriterService implements WriterService {
    private final MessageService messageService;

    @Override
    public String writeMessage() {
        return "writing message... " + messageService.getMessage();
    }
}
