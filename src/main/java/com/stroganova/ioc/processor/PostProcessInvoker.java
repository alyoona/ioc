package com.stroganova.ioc.processor;

import com.stroganova.ioc.entity.Bean;
import com.stroganova.ioc.entity.BeanPostProcessor;

import java.util.List;

public class PostProcessInvoker implements Invoker {

    private List<Bean> systemBeans;
    private List<Bean> beans;
    private String methodName;


    public PostProcessInvoker(List<Bean> systemBeans, List<Bean> beans, String methodName) {
        this.systemBeans = systemBeans;
        this.beans = beans;
        this.methodName = methodName;
    }

    @Override
    public void invoke() {
        for (Bean systemBean : systemBeans) {
            BeanPostProcessor beanPostProcessor = (BeanPostProcessor) systemBean.getValue();
            for (Bean bean : beans) {
                Object newValue = null;
                if ("postProcessBeforeInitialization".equals(methodName)) {
                    newValue = beanPostProcessor.postProcessBeforeInitialization(bean.getValue(), bean.getId());
                } else if ("postProcessAfterInitialization".equals(methodName)) {
                    newValue = beanPostProcessor.postProcessAfterInitialization(bean.getValue(), bean.getId());
                }
                bean.setValue(newValue);
            }
        }
    }
}
