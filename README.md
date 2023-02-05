# bring-ioc

## Bring-IoC is a framework that provides an Inversion of Control container.

* [Github link](https://github.com/petrosteam/bring-ioc/blob/main/README.md)

## How to start ?

* Import to your Maven project dependency:\
  \<dependency>\
  &nbsp;&nbsp;&nbsp;&nbsp;\<groupId>ioc\</groupId>\
  &nbsp;&nbsp;&nbsp;&nbsp;\<artifactId>bring-ioc\</artifactId>\
  &nbsp;&nbsp;&nbsp;&nbsp;\<version>1.0-SNAPSHOT\</version>\
  \</dependency>


* Create an instance of BeanFactory.class and set a path to package to scan as an argument.\
  ex.: BeanFactory beanFactory = Application.run("com.my.package")

### Context of Application is based on @Annotations over classes, fields and methods.

## Annotations

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