package com.stroganova.ioc.processor;

import com.stroganova.ioc.entity.Bean;
import com.stroganova.ioc.entity.BeanPostProcessor;

import java.util.List;

public class PostProcessAfterInvoker implements Invoker{

    private List<Bean> systemBeans;
    private List<Bean> beans;

    public PostProcessAfterInvoker(List<Bean> systemBeans, List<Bean> beans) {
        this.systemBeans = systemBeans;
        this.beans = beans;
    }

    @Override
    public void invoke() {
        for (Bean systemBean : systemBeans) {
            BeanPostProcessor beanPostProcessor = (BeanPostProcessor) systemBean.getValue();
            for (Bean bean : beans) {
                Object newValue = beanPostProcessor.postProcessAfterInitialization(bean.getValue(), bean.getId());
                bean.setValue(newValue);
            }
        }
    }

}
