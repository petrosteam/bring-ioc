package com.petros.client.services;

import com.petros.bring.annotations.Autowire;
import com.petros.bring.annotations.Service;

@Service
public class ServiceWithDependency {

    @Autowire
    ServiceSimple s2;
    public String getServiceName() {
        System.out.println("s1 service");
        System.out.println(s2.getServiceName() + " included");
        return "s1+s2";
    }

}
