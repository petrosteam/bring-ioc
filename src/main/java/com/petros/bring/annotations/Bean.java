package com.petros.bring.annotations;

import com.petros.bring.reader.Scope;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Bean {
    String name() default StringUtils.EMPTY;

    Scope scope() default Scope.SINGLETON;
}
