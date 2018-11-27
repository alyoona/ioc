package com.stroganova.ioc.processor;

import com.stroganova.ioc.entity.Bean;
import com.stroganova.ioc.entity.BeanPostProcessor;

import java.util.List;

public class PostProcessBeforeInvoker implements Invoker{

    private List<Bean> systemBeans;
    private List<Bean> beans;

    public PostProcessBeforeInvoker(List<Bean> systemBeans, List<Bean> beans) {
        this.systemBeans = systemBeans;
        this.beans = beans;

    }

    @Override
    public void invoke() {
        for (Bean systemBean : systemBeans) {
            BeanPostProcessor beanPostProcessor = (BeanPostProcessor) systemBean.getValue();
            for (Bean bean : beans) {
                Object newValue = beanPostProcessor.postProcessBeforeInitialization(bean.getValue(), bean.getId());
                bean.setValue(newValue);
            }
        }
    }

}
