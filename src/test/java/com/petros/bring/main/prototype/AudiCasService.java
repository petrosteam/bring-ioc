package com.petros.bring.main.prototype;

import com.petros.bring.annotations.Component;
import com.petros.bring.reader.Scope;

@Component(scope = Scope.PROTOTYPE)
public class AudiCasService implements CarService {
    @Override
    public String getCar() {
        return "audi";
    }
}
