package com.petros.bring.annotations;

import com.petros.bring.reader.Scope;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that method produces bean that can be managed by Bring context.
 * This annotation can be used only in classes annotated @Configuration
 * Example:
 * <pre>{@code
 * @Configuration
 * public class SomeConfiguration {
 *  @Bean(name = "beanName")
 * 	public MessageService messageService() {
 * 		return new MessageServiceImpl();
 *  }
 * }}</pre>
 * Bean with name "beanName" will be placed into context.
 * If name attribute of @Bean annotation is empty, then method's name will be used for bean naming
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Bean {
    String name() default StringUtils.EMPTY;

    Scope scope() default Scope.SINGLETON;
}
