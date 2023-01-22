package com.petros.client;

import com.petros.bring.ApplicationContext;
import com.petros.client.services.ServiceWithDependency;

public class BringClient {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ApplicationContext("com.petros.client");
        ServiceWithDependency serviceWithDependency = applicationContext.getBean(ServiceWithDependency.class);
        System.out.println("client access to " + serviceWithDependency.getServiceName());
    }
}
