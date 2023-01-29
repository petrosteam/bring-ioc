package com.pertos.bring.reader;

/**
 * A bean definition describes the properties of the bean and rules of how it should be created
 */
public interface BeanDefinition {
    /**
     * Return the name of the bean
     */
    String getName();

    /**
     * Return the name of class the bean should be instantiated from
     */
    String getBeanClassName();

    /**
     * Return a scope of the current bean
     */
    Scope getScope();

    /**
     * Return whether the bean should be instantiated lazily
     */
    boolean isLazy();

    /**
     * Return whether the bean should be considered as a prime candidate for autowiring
     */
    boolean isPrimary();

    /**
     * Return an array of bean names, current bean depends on
     */
    String[] getDependsOn();
}
