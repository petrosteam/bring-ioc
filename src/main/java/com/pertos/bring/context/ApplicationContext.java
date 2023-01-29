package com.pertos.bring.context;

import com.pertos.bring.exception.NoSuchBeanException;
import com.pertos.bring.exception.NoUniqueBeanException;
import com.pertos.bring.exception.BeanNotOfRequiredTypeException;

import java.util.Map;

/**
 * Central interface for providing access to a bean container.
 *
 * <p>An ApplicationContext provides methods for accessing application components.
 * Depending on the bean definition, container will return a single shared object (singleton) or
 * an independent instance of contained object (prototype)
 *
 * @author Mykola Demchenko
 */
public interface ApplicationContext {

    /**
     * Return the bean instance of required type.
     * It can be singleton created when container is started or prototype,
     * object instantiated every time the method is invoked.
     *
     * @param beanType type the bean must match; can be an interface or superclass
     * @return an instance of the bean to return
     * @throws NoSuchBeanException if there is no such bean
     * @throws NoUniqueBeanException if there is more than one bean of required type
     */
    <T> T getBean(Class<T> beanType) throws NoSuchBeanException, NoUniqueBeanException;

    /**
     * Retrieve bean instance from context by name.
     * If bean exist, it is cast and returned.
     *
     * @param name the name of the bean to return
     * @param beanType type the bean must match; can be an interface or superclass
     * @return an instance of the bean
     * @throws NoSuchBeanException if there is no such bean
     * @throws BeanNotOfRequiredTypeException if the bean is not of the required type
     */
    <T> T getBean(String name, Class<T> beanType);

    /**
     * Return all bean instances of required type
     *
     * @param beanType type the bean must match; can be an interface or superclass
     * @return a map of bean instances by its name
     */
    <T> Map<String, T> getAllBeans(Class<T> beanType);
}
