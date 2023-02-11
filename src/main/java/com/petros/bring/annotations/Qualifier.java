package com.petros.bring.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Qualifier - annotation that may be used on a field only to clarify which bean to inject from the context.
 * <p>
 * Using with {@link Autowired} annotation.
 * <p>
 * Usage example:
 * <pre>{@code
 * public class JavadocExampleClass {
 *
 *   @Autowired
 *   @Qualifier("distinctClassName")
 *   public ExampleService exampleService;
 * }}</pre>
 */
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Qualifier {
    String value() default "";
}
