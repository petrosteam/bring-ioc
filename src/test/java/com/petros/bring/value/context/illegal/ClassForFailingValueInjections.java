package com.petros.bring.value.context.illegal;

import com.petros.bring.annotations.Component;
import com.petros.bring.annotations.Value;

import java.math.BigDecimal;

@Component
public class ClassForFailingValueInjections {

    @Value("123L")
    private BigDecimal value;
}
