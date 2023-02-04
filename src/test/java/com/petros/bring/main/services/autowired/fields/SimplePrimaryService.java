package com.petros.bring.main.services.autowired.fields;

import com.petros.bring.annotations.Component;
import com.petros.bring.annotations.Lazy;
import com.petros.bring.annotations.Primary;
import com.petros.bring.main.services.Service;

@Primary
@Component(name = "booblik")
@Lazy
public class SimplePrimaryService implements Service {
    @Override
    public Service getService() {
        return null;
    }
}
