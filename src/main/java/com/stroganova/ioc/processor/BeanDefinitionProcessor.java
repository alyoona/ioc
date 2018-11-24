package com.stroganova.ioc.processor;

import com.stroganova.ioc.entity.BeanDefinition;
import com.stroganova.ioc.entity.BeanFactoryPostProcessor;
import com.stroganova.ioc.entity.BeanPostProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanDefinitionProcessor {

    private List<BeanDefinition> allBeanDefinitions;

    private Map<String, List<BeanDefinition>> allBeanDefinitionsMap = new HashMap<>();

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

        allBeanDefinitionsMap.put("beanDefinitions", beanDefinitions);
        allBeanDefinitionsMap.put("beanFactoryPostProcessorDefinitions", beanFactoryPostProcessorDefinitions);
        allBeanDefinitionsMap.put("beanPostProcessorDefinitions", beanPostProcessorDefinitions);

    }

    public Map<String, List<BeanDefinition>> getAllBeanDefinitionsMap() {
        return allBeanDefinitionsMap;
    }
}