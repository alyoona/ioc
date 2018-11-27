package com.stroganova.ioc.processor;

import com.stroganova.ioc.entity.Bean;
import com.stroganova.ioc.entity.PostConstruct;

import java.lang.reflect.Method;
import java.util.List;

public class PostConstructInvoker implements Invoker {

    private List<Bean> beans;

    public PostConstructInvoker(List<Bean> beans) {
        this.beans = beans;
    }


    @Override
    public void invoke() {
        for (Bean bean : beans) {
            Object object = bean.getValue();
            Class<?> clazz = object.getClass();
            try {
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.getDeclaredAnnotation(PostConstruct.class) != null) {
                        method.setAccessible(true);
                        method.invoke(object);
                        method.setAccessible(false);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
