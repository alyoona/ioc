package com.stroganova.ioc.processor;

import com.stroganova.ioc.entity.Bean;
import com.stroganova.ioc.entity.BeanDefinition;
import com.stroganova.ioc.entity.BeanFactoryPostProcessor;

import java.util.List;

public class PostProcessBeanFactoryInvoker implements Invoker  {

    private List<Bean> systemBeans;
    private List<BeanDefinition> beanDefinitions;


    public PostProcessBeanFactoryInvoker(List<Bean> systemBeans, List<BeanDefinition> beanDefinitions) {
        this.systemBeans = systemBeans;
        this.beanDefinitions = beanDefinitions;
    }

    public void invoke() {
        for(Bean systemBean: systemBeans) {
            BeanFactoryPostProcessor beanFactoryPostProcessor = (BeanFactoryPostProcessor) systemBean.getValue();
            beanFactoryPostProcessor.postProcessBeanFactory(beanDefinitions);
        }
    }
}
