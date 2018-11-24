package com.stroganova.ioc.entity;

public interface BeanPostProcessor {

    Object postProcessBeforeInitialization(Object bean, String id);

    Object postProcessAfterInitialization(Object bean, String id);

}
