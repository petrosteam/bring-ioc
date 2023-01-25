package com.pertos.bring.reader;

public interface BeanDefinition {

    String getName();

    String getBeanClassName();

    Scope getScope();

    boolean isLazy();

    boolean isPrimary();

    String[] getDependsOn();
}
