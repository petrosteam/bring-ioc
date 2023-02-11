package com.petros.bring.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that sets a value to a variable of a Class. There are 2 ways of usage:
 *
 * Set direct value of type String or any Number which is analogue of Setter method.
 * Examples of usage:
 * <pre>{@code
 * @Value("myStringValue")
 * String variable;
 * @Value("123")
 * Integer variable;
 * @Value("132.02")
 * double variable;}</pre>
 * Set value that should be taken from ApplicationProperties or default.
 * Examples of usage:
 * <pre>{@code
 * @Value("${myPropertyName}")
 * String variable;
 * @Value("${myPropertyName:155.0")
 * double variable;}</pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Value {
    String value();
}
