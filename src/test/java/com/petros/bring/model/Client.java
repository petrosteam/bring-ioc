package com.petros.bring.model;

import com.petros.bring.Application;
import com.petros.bring.bean.factory.BeanFactory;

public class Client {
    public static void main(String[] args) {
        BeanFactory beanFactory = Application.run("com.petros.bring");
        System.out.println(beanFactory.getBean(SimpleService.class));
        System.out.println(beanFactory.getAllBeans(Service.class));
    }
}