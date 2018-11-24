package com.stroganova.ioc.service;

import com.stroganova.ioc.entity.BeanPostProcessor;

public class BPPClassTwo  implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String id) {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String id) {
        return bean;
    }
}
