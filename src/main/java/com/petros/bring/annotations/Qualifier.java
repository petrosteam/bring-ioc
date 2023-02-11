package com.petros.bring.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Indicates name of the bean that should be taken when multiple candidates are qualified to autowire
 * Example:
 * <pre>{@code
 * @Component(name = "hello")
 * public class HelloMessageService implements MessageService {...}
 * ...
 * @Component(name = "goodbye")
 * public class GoodbyeMessageService implements MessageService {...}
 * ...
 * @Component
 * public class SomeSenderService {
 *  @Autowired
 *  @Qualifier("hello")
 *  private MessageService messageService;
 *  ...
 * }
 * }</pre>
 * Instance of class HelloMessageService will be autowired. If @Qualifier is omitted, then NoUniqueBeanException is thrown.
 */
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Qualifier {
    String value() default "";
}
