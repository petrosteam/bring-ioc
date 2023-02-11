package com.petros.bring.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation allows to mark a field property or a method with only one class as parameter.
 * Marked variable or a parameter must be a class.
 * <p>
 * Marked class will be taken from an application context loaded by {@link com.petros.bring.bean.factory.BeanFactory}.
 * <p>Injected field of a method must be used in classes annotated with: {@link Component} or {@link Configuration}.
 * <p>
 * To inject class with a certain name - use {@link Qualifier}.
 * <p>
 * Example of field autowiring:
 * <pre>{@code
 * public class JavadocExampleClass {
 *
 *   @Autowired
 *   public ExampleService exampleService;
 * }}</pre>
 * <p>
 * Example of method autowiring:
 * <pre>{@code
 * public class JavadocExampleClass {
 *
 *   private ExampleService exampleService;
 *
 *   @Autowired
 *   public void setExampleService(ExampleService exampleService) {
 *      this.exampleService = exampleService;
 *   }
 * }}</pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Autowired {
}
