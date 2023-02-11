package com.petros.bring.annotations;

import com.petros.bring.reader.Scope;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.*;

/**
 * Indicates that instance of class annotated @Component will be created and placed in context.
 * Example:
 * <pre>{@code
 * @Component(name = "beanName", scope = Scope.PROTOTYPE)
 * public class SomeComponent {
 *     ...
 * }}</pre>
 * Bean with name "beanName" will be managed by Bring context.
 * Argument scope can have two possible values:
 * SINGLETON - means that bean of class will be created on application start up and placed in context;
 * PROTOTYPE - every time context will be asked to provide a bean of such class, then new instance will be created.
 * It means that prototype beans are not stored in context.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface Component {
    String name() default StringUtils.EMPTY;

    Scope scope() default Scope.SINGLETON;
}
