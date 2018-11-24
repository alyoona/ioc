package com.stroganova.ioc.service;

import com.stroganova.ioc.entity.PostConstruct;

public class PostConstructService {

    private String status;

    @PostConstruct
    public void init() {

        status = "init method has been called";
    }

    public String getStatus() {
        return status;
    }

}
