package com.petros.bring.main.prototype;

import com.petros.bring.annotations.Component;
import com.petros.bring.annotations.Primary;
import com.petros.bring.reader.Scope;

@Component(scope = Scope.SINGLETON)
@Primary
public class KiaCarService implements CarService {
    @Override
    public String getCar() {
        return "kia";
    }
}
