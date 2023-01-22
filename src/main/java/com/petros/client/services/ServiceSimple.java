package com.petros.client.services;

import com.petros.bring.annotations.Service;

@Service
public class ServiceSimple {
    public String getServiceName() {
        System.out.println("s2 service");
        return "s2";
    }
}
