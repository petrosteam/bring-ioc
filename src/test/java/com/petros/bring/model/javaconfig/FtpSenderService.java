package com.petros.bring.model.javaconfig;

import com.petros.bring.annotations.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FtpSenderService implements SenderService {
    private final MessageService messageService;

    @Override
    public String sendMessage() {
        return "sending via ftp..." + messageService.getMessage();
    }
}
