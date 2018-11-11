package com.stroganova.ioc.reader;

import com.stroganova.ioc.entity.BeanDefinition;

import java.util.List;

public interface BeanDefinitionReader {

    List<BeanDefinition> readBeanDefinitions();

    void setPath(String path);

}
