package com.stroganova.ioc.processor;

import com.stroganova.ioc.entity.Bean;
import com.stroganova.ioc.entity.BeanDefinition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanProcessor {

    private Map<String, Bean> beans = new HashMap<>();
    private Map<Class, String> beansIdByType = new HashMap<>();
    private Map<Class, Integer> beansCountByType = new HashMap<>();


    public void process(List<BeanDefinition> beanDefinitionsList) {
        for (BeanDefinition beanDefinition : beanDefinitionsList) {
            String id = beanDefinition.getId();
            Bean bean = new Bean();
            bean.setId(id);
            try {
                Class clazz = Class.forName(beanDefinition.getBeanClassName());
                bean.setValue(clazz.newInstance());
                beans.put(id, bean);
                setBeanTypeAndCount(clazz, id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void setBeanTypeAndCount(Class clazz, String id) {
        Integer count = beansCountByType.get(clazz);
        if (count != null) {
            count++;
            beansIdByType.put(clazz, null);
        } else {
            count = 1;
            beansIdByType.put(clazz, id);
        }
        beansCountByType.put(clazz, count);
    }


    public Map<String, Bean> getBeans() {
        return beans;
    }

    public Map<Class, String> getBeansIdByType() {
        return beansIdByType;
    }

    public Map<Class, Integer> getBeansCountByType() {
        return beansCountByType;
    }


}
