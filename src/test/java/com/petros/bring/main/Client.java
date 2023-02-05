package com.petros.bring.main;

import com.petros.bring.Application;
import com.petros.bring.bean.factory.BeanFactory;
import com.petros.bring.main.services.autowired.fields.ComplexServiceWithSimpleService;
import com.petros.bring.main.services.Service;
import com.petros.bring.main.services.autowired.fields.SimplePrimaryService;

public class Client {
    public static void main(String[] args) {
        BeanFactory beanFactory = Application.run("com.petros.bring");
        System.out.println(beanFactory.getBean(SimplePrimaryService.class));
        System.out.println(beanFactory.getAllBeans(Service.class));
        ComplexServiceWithSimpleService complexServiceWithSimpleService = beanFactory.getBean(ComplexServiceWithSimpleService.class);
    }
}