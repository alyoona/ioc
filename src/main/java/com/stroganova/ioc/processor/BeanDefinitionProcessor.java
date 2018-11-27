package com.stroganova.ioc.processor;

import com.stroganova.ioc.entity.BeanDefinition;
import com.stroganova.ioc.entity.BeanFactoryPostProcessor;
import com.stroganova.ioc.entity.BeanPostProcessor;
import com.stroganova.ioc.entity.BeanType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanDefinitionProcessor {

    private List<BeanDefinition> allBeanDefinitions;

    private Map<BeanType, List<BeanDefinition>> allBeanDefinitionsMap = new HashMap<>();

    public BeanDefinitionProcessor(List<BeanDefinition> allBeanDefinitions) {
        this.allBeanDefinitions = allBeanDefinitions;
        process();
    }

    private void process() {
        List<BeanDefinition> beanDefinitions = new ArrayList<>();
        List<BeanDefinition> beanFactoryPostProcessorDefinitions = new ArrayList<>();
        List<BeanDefinition> beanPostProcessorDefinitions = new ArrayList<>();

        for (BeanDefinition beanDefinition : allBeanDefinitions) {
            try {
                Class clazz = Class.forName(beanDefinition.getBeanClassName());
                Class[] interfaces = clazz.getInterfaces();
                if (interfaces.length > 0) {
                    for (Class interfaceValue : interfaces) {
                        if (interfaceValue.equals(BeanFactoryPostProcessor.class)) {
                            beanFactoryPostProcessorDefinitions.add(beanDefinition);
                        } else if (interfaceValue.equals(BeanPostProcessor.class)) {
                            beanPostProcessorDefinitions.add(beanDefinition);
                        }
                    }
                } else {
                    beanDefinitions.add(beanDefinition);
                }


            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        allBeanDefinitionsMap.put(BeanType.BUSINESS, beanDefinitions);
        allBeanDefinitionsMap.put(BeanType.BEAN_FACTORY_POST_PROCESSOR, beanFactoryPostProcessorDefinitions);
        allBeanDefinitionsMap.put(BeanType.BEAN_POST_PROCESSOR, beanPostProcessorDefinitions);

    }

    public Map<BeanType, List<BeanDefinition>> getAllBeanDefinitionsMap() {
        return allBeanDefinitionsMap;
    }
}