package com.petros.client;

import com.petros.bring.ApplicationContext;
import com.petros.client.services.Service1;

public class BringClient {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ApplicationContext("com.petros.client");
        Service1 service1 = applicationContext.getBean(Service1.class);
        System.out.println("client access to " + service1.getServiceName());
    }
}
