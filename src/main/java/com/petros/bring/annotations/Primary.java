package com.petros.bring.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that bean of this class should be taken when multiple candidates are qualified to autowire
 * Example:
 * <pre>{@code
 * @Primary
 * @Component
 * public class HelloMessageService implements MessageService {...}
 * ...
 * @Component
 * public class GoodbyeMessageService implements MessageService {...}
 * ...
 * @Component
 * public class SomeSenderService {
 *  @Autowired
 *  private MessageService messageService;
 *  ...
 * }
 * }</pre>
 * Instance of class HelloMessageService will be autowired. If @Primary is omitted, then NoUniqueBeanException is thrown
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Primary {
}
