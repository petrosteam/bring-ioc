package com.petros.client.services;

import com.petros.bring.annotations.Service;

@Service
public class Service2 {
    public String getServiceName() {
        System.out.println("s2 service");
        return "s2";
    }
}
