package com.petros.bring.model.javaconfig;

public class HttpSenderService implements SenderService {
    private final MessageService messageService;

    public HttpSenderService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public String sendMessage() {
        return "sending message... " + messageService.getMessage();
    }
}
