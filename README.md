# bring-ioc

## Bring-IoC is a framework that provides an Inversion of Control container.

* [Github link](https://github.com/petrosteam/bring-ioc/blob/main/README.md)

## How to start ?

* Clone GitHub repository by link [https://github.com/petrosteam/bring-ioc.git](https://github.com/petrosteam/bring-ioc.git)
* Open the cloned repository in your IDE and run 'mvn clean install'.
* Import to your Maven project dependency:

```<dependency>
  <groupId>ioc</groupId>
  <artifactId>bring-ioc</artifactId>
  <version>1.0-SNAPSHOT</version>
  </dependency>
  ```

* Create an instance of BeanFactory.class and set a path to package to scan as an argument.\
  ex.: BeanFactory beanFactory = Application.run("com.my.package")

### Context of Application is based on @Annotations over classes, fields and methods.

## Annotations

### @Autowired

@Autowired annotation allows to mark a field property or a method with only one class as parameter. 
Marked variable or a parameter must be a class.

Marked class will be taken from an application context loaded by com.petros.bring.bean.factory.BeanFactory. 
Injected field of a method must be used in classes annotated with: Component or Configuration.
To inject class with a certain name - use Qualifier.
Example of field autowiring:
```java
@Component
public class JavadocExampleClass { 
    @Autowired 
    public ExampleService exampleService; 
}
```
Example of method autowiring:

```java
import com.petros.bring.annotations.Component;

@Component
public class JavadocExampleClass {
    private ExampleService exampleService;

    @Autowired
    public void setExampleService(ExampleService exampleService) {
        this.exampleService = exampleService;
    }
}
```

### @Qualifier

@Qualifier is annotation that may be used on a field only to clarify which bean to inject from the context.

Using with [@Autowired](#autowired) annotation.

Usage example:
```java
public class JavadocExampleClass {
    @Autowired
    @Qualifier("distinctClassName")
    public ExampleService exampleService;
}
```
### @Value

@Value is an annotation that sets a value to a variable of a Class. There are 2 ways of usage:

1. Set direct value of type String or any Number which is analogue of Setter method.\
   Examples of usage:
    1. @Value("myStringValue")\
       String variable;
    2. @Value("123")\
       Integer variable;
    3. @Value("132.02")\
       double variable;
2. Set value that should be taken from ApplicationProperties or default.\
   Examples of usage:
   1. @Value("${myPropertyName}")\
      String variable; 
   2. @Value("${myPropertyName:155.0")\
   double variable;

### Injecting BRING beans in your project
You could inject any beans from packages:
* com.petros.bring.reader
* com.petros.bring.context
* com.petros.bring.postprocessor
* com.petros.bring.environment

### Creating custom BeanPostProcessor
This implementation scans internal packages on the startup and allows you to extend basic features by adding custom 
bean post processor

```java
package org.example.sandbox;

import com.petros.bring.annotations.Autowired;
import com.petros.bring.annotations.Component;
import com.petros.bring.bean.factory.BeanFactory;
import com.petros.bring.context.AnnotationConfigApplicationContext;
import com.petros.bring.postprocessor.BeanPostProcessor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Component
@Slf4j
public class LoggingBeanPostProcessor implements BeanPostProcessor {
    private final BeanFactory factory;

    public LoggingBeanPostProcessor(AnnotationConfigApplicationContext beanFactory) {
        this.factory = beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Class<?> beanClass, Object bean) {
        log.info("This is custom post-processor for bean type{} ", beanClass.getSimpleName());
        return bean;
    }
}

```

When you add this class to package you want to scan, all beans will be handled with your custom bean post processor 
implementation.