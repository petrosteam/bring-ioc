package com.petros.bring.main.image;

import com.petros.bring.annotations.Component;

@Component
public class MoonImageService implements ImageService {
    @Override
    public String getImage() {
        return "moon image";
    }
}
