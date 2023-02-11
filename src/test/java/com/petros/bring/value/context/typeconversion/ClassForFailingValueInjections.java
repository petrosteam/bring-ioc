package com.petros.bring.value.context.typeconversion;

import com.petros.bring.annotations.Component;
import com.petros.bring.annotations.Value;

@Component
public class ClassForFailingValueInjections {

    @Value("123L")
    private Object value;
}
