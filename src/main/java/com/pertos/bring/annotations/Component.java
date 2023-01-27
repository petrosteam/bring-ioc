package com.pertos.bring.annotations;

import com.pertos.bring.reader.Scope;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Component {
    String name() default StringUtils.EMPTY;
    Scope scope() default Scope.SINGLETON;
}
