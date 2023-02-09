package com.petros.bring.model.autowiring.constructor.normal;

import com.petros.bring.annotations.Component;

@Component
public class FtpSenderService implements SenderService {
    private final MessageService messageService;

    public FtpSenderService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public String sendMessage() {
        return "Sending via ftp... " + messageService.getMessage();
    }
}
