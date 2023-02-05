package com.petros.bring.model.configuration;

import com.petros.bring.annotations.Bean;
import com.petros.bring.annotations.Configuration;

@Configuration
public class JavaConfiguration {
    @Bean
    public A beanA() {
        return new ADependsOnB(beanB());
    }

    @Bean
    public B beanB() {
        return new BIndependent();
    }
}
