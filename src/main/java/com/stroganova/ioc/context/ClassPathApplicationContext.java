package com.stroganova.ioc.context;

import com.stroganova.ioc.entity.Bean;
import com.stroganova.ioc.entity.BeanDefinition;
import com.stroganova.ioc.entity.BeanStatus;
import com.stroganova.ioc.exception.NoUniqueBeanException;
import com.stroganova.ioc.exception.NotFoundBeanException;
import com.stroganova.ioc.injector.DependencyInjector;
import com.stroganova.ioc.injector.RefDependencyInjector;
import com.stroganova.ioc.processor.BeanProcessor;
import com.stroganova.ioc.reader.BeanDefinitionReader;
import com.stroganova.ioc.reader.XMLBeanDefinionReader;

import java.util.*;

public class ClassPathApplicationContext implements ApplicationContext {

    private String[] path;

    private List<BeanDefinition> beanDefinitionsList;
    private Map<String, Bean> beans;
    private Map<Class, String> beansIdByType;
    private Map<Class, Integer> beansCountByType;

    public ClassPathApplicationContext(String[] path) {
        this.path = path;
        init();
    }

    public ClassPathApplicationContext(String path) {
        this(new String[]{path});
    }


    private void init() {
        BeanDefinitionReader reader = new XMLBeanDefinionReader();
        reader.setPath(path[0]);
        beanDefinitionsList = reader.readBeanDefinitions();
        createBeansFromBeanDefinitions();
        injectDependencies();
        injectRefDependencies();
    }

    private void createBeansFromBeanDefinitions() {
        BeanProcessor beanProcessor = new BeanProcessor();
        beanProcessor.process(beanDefinitionsList);
        beans = beanProcessor.getBeans();
        beansIdByType = beanProcessor.getBeansIdByType();
        beansCountByType = beanProcessor.getBeansCountByType();

    }

    private void injectDependencies() {
        for (BeanDefinition beanDefinition : beanDefinitionsList) {
            Map<String, String> dependencies = beanDefinition.getDependencies();
            if (dependencies != null) {
                String id = beanDefinition.getId();
                Bean bean = beans.get(id);
                DependencyInjector dependencyInjector = new DependencyInjector();
                dependencyInjector.inject(bean, dependencies);
            }
        }
    }

    private void injectRefDependencies() {
        for (BeanDefinition beanDefinition : beanDefinitionsList) {
            Map<String, String> refDependencies = beanDefinition.getRefDendencies();
            if (refDependencies != null) {
                String id = beanDefinition.getId();
                Bean bean = beans.get(id);
                RefDependencyInjector refDependencyInjector = new RefDependencyInjector();
                refDependencyInjector.inject(bean, refDependencies, beans);
            }
        }
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        BeanStatus beanStatus = getStatus(clazz);
        if (BeanStatus.MORE_THAN_ONE.equals(beanStatus)) {
            throw new NoUniqueBeanException("More than one bean has been instantiated for this type: " + clazz.getName());
        } else if (BeanStatus.NOT_FOUND.equals(beanStatus)) {
            throw new NotFoundBeanException("Nothing have been instantiated with type: " + clazz.getName());
        }
        String id = beansIdByType.get(clazz);
        return clazz.cast(beans.get(id).getValue());
    }

    @Override
    public <T> T getBean(String name, Class<T> clazz) {
        Bean bean = beans.get(name);
        if (bean != null) {
            Object beanValue = bean.getValue();
            if (clazz == beanValue.getClass()) {
                return clazz.cast(beanValue);
            }
        }
        throw new NotFoundBeanException("Bean hasn't been instantiated with name: " + name + " and type: " + clazz.getName());
    }

    @Override
    public Object getBean(String name) {
        Bean bean = beans.get(name);
        if (bean != null) {
            return bean.getValue();
        }
        throw new NotFoundBeanException("Bean hasn't been instantiated with name: " + name);
    }

    @Override
    public List<String> getBeanNames() {
        return new ArrayList<>(beans.keySet());
    }

    private BeanStatus getStatus(Class clazz) {
        Integer count = beansCountByType.get(clazz);
        if (count != null) {
            if (count > 1) {
                return BeanStatus.MORE_THAN_ONE;
            }
            return BeanStatus.FOUND;
        }
        return BeanStatus.NOT_FOUND;
    }

}
