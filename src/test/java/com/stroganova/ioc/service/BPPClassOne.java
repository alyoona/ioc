package com.stroganova.ioc.service;

import com.stroganova.ioc.entity.BeanPostProcessor;

import java.util.ArrayList;
import java.util.TreeSet;

public class BPPClassOne implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String id) {
        if("someService".equals(id) && bean instanceof SomeService) {
            return new ArrayList<>();
        }
        return bean;
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String id) {

        if("someService".equals(id) && bean instanceof SomeService) {
            return new TreeSet<>();
        }
        return bean;

    }
}
