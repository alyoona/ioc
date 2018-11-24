package com.stroganova.ioc.processor;

import com.stroganova.ioc.entity.Bean;
import com.stroganova.ioc.entity.BeanDefinition;

import java.util.Map;

class TestUtil {

    static BeanDefinition getBeanDefinition(String id, String className, Map<String, String> deps, Map<String, String> refs){
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setId(id);
        beanDefinition.setBeanClassName(className);
        beanDefinition.setDependencies(deps);
        beanDefinition.setRefDendencies(refs);
        return beanDefinition;
    }

    static Bean getBean(Object value, String id) {
        Bean systemBean = new Bean();
        systemBean.setValue(value);
        systemBean.setId(id);
        return systemBean;
    }

}
