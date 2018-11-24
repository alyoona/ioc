package com.stroganova.ioc.service;

import com.stroganova.ioc.entity.BeanDefinition;
import com.stroganova.ioc.entity.BeanFactoryPostProcessor;

import java.util.List;

public class BFPPClassOne implements BeanFactoryPostProcessor {

    private String status;

    @Override
    public void postProcessBeanFactory(List<BeanDefinition> definitions) {
        status = "definitions have been processed by BFPPClassOne before creating beans";
    }

    public String getStatus() {
        return status;
    }
}
