package com.petros.bring.main.image;

import com.petros.bring.annotations.Component;

@Component
public class MarsImageService implements ImageService {
    @Override
    public String getImage() {
        return "mars image";
    }
}
