package com.petros.bring.annotations;

import com.petros.bring.reader.Scope;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface Component {
    String name() default StringUtils.EMPTY;
    Scope scope() default Scope.SINGLETON;
}
