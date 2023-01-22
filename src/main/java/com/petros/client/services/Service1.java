package com.petros.client.services;

import com.petros.bring.annotations.Autowire;
import com.petros.bring.annotations.Service;

@Service
public class Service1 {

    @Autowire
    Service2 s2;
    public String getServiceName() {
        System.out.println("s1 service");
        System.out.println(s2.getServiceName() + " included");
        return "s1+s2";
    }

}
