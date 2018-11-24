package com.stroganova.ioc.context;

import com.stroganova.ioc.entity.Bean;
import com.stroganova.ioc.entity.BeanDefinition;
import com.stroganova.ioc.entity.BeanStatus;
import com.stroganova.ioc.exception.NoUniqueBeanException;
import com.stroganova.ioc.exception.NotFoundBeanException;
import com.stroganova.ioc.injector.DependencyInjector;
import com.stroganova.ioc.injector.RefDependencyInjector;
import com.stroganova.ioc.processor.*;
import com.stroganova.ioc.processor.BeanDefinitionProcessor;
import com.stroganova.ioc.reader.BeanDefinitionReader;
import com.stroganova.ioc.reader.XMLBeanDefinionReader;

import java.util.*;

public class ClassPathApplicationContext implements ApplicationContext {

    private String[] paths;
    private Map<String, List<BeanDefinition>> allBeanDefinitions;

    private Map<String, Bean> beans;
    private Map<Class<?>, String> beansIdByType;
    private Map<Class<?>, Integer> beansCountByType;
    private List<Bean> beansList;
    private List<Bean> systemBeansFactoryPostProcessor;
    private List<Bean> systemBeansPostProcessor;

    public ClassPathApplicationContext(String... paths) {
        this.paths = paths;
        init();
    }

    public ClassPathApplicationContext(String path) {
        this(new String[]{path});
    }


    private void init() {
        /*1. read bean definitions*/
        readBeabDefinitions();

        /*1.1 BeanFactoryPostProcessor*/
        createSystemBeansOfBeanFactoryPostProcessor();
        callPostProcessBeanFactory();

        /*2. construct beans from bean definitions (empty objects created with newInstance)*/
        createBeansFromBeanDefinitions();

        /*3. injectValueDependencies -> inject primitives and strings*/
        injectDependencies();

        /*4. injectRefDependencies -> inject refs to beans*/
        injectRefDependencies();

        /*5. BeanPostProcessor */
        createSystemBeansOfBeanPostProcessor();

        /*5.1 BeanPostProcessor postProcessBeforeInitialization*/
        callPostProcessBeforeInitialization();

        /*6. call init method(@PostProcess)*/
        callInitMethodOfBeansAnnotatedByPostProcess();

        /*7. BeanPostProcessor postProcessAfterInitialization*/
        callPostProcessAfterInitialization();


    }

    /*1. read bean definitions*/

    private void readBeabDefinitions() {
        BeanDefinitionReader reader = new XMLBeanDefinionReader();
        List<BeanDefinition> beanDefinitionsList = new ArrayList<>();
        for (String path : paths) {
            reader.setPath(path);
            beanDefinitionsList.addAll(reader.readBeanDefinitions());
        }
        BeanDefinitionProcessor beanDefinitionProcessor = new BeanDefinitionProcessor(beanDefinitionsList);
        allBeanDefinitions = beanDefinitionProcessor.getAllBeanDefinitionsMap();
    }


    /*1.1 BeanFactoryPostProcessor*/
    private void createSystemBeansOfBeanFactoryPostProcessor() {
        BeanProcessor beanProcessor = new BeanProcessor(allBeanDefinitions.get("beanFactoryPostProcessorDefinitions"));
        systemBeansFactoryPostProcessor = beanProcessor.getBeansList();
    }

    private void callPostProcessBeanFactory() {
        Invoker invoker = new PostProcessBeanFactoryInvoker(systemBeansFactoryPostProcessor, allBeanDefinitions.get("beanDefinitions"));
        invoker.invoke();
    }

    /*2. construct beans from bean definitions (empty objects created with newInstance)*/
    private void createBeansFromBeanDefinitions() {
        BeanProcessor beanProcessor = new BeanProcessor(allBeanDefinitions.get("beanDefinitions"));
        beans = beanProcessor.getBeans();
        beansIdByType = beanProcessor.getBeansIdByType();
        beansCountByType = beanProcessor.getBeansCountByType();
        beansList = beanProcessor.getBeansList();
    }

    /*3. injectValueDependencies -> inject primitives and strings*/
    private void injectDependencies() {
        DependencyInjector dependencyInjector = new DependencyInjector();
        for (BeanDefinition beanDefinition : allBeanDefinitions.get("beanDefinitions")) {
            Map<String, String> dependencies = beanDefinition.getDependencies();
            if (dependencies != null) {
                String id = beanDefinition.getId();
                Bean bean = beans.get(id);
                dependencyInjector.inject(bean, dependencies);
            }
        }
    }

    /*4. injectRefDependencies -> inject refs to beans*/
    private void injectRefDependencies() {
        RefDependencyInjector refDependencyInjector = new RefDependencyInjector();
        for (BeanDefinition beanDefinition : allBeanDefinitions.get("beanDefinitions")) {
            Map<String, String> refDependencies = beanDefinition.getRefDendencies();
            if (refDependencies != null) {
                String id = beanDefinition.getId();
                Bean bean = beans.get(id);
                refDependencyInjector.inject(bean, refDependencies, beans);
            }
        }
    }

    /*5. BeanPostProcessor */
    private void createSystemBeansOfBeanPostProcessor() {
        BeanProcessor beanProcessor = new BeanProcessor(allBeanDefinitions.get("beanPostProcessorDefinitions"));
        systemBeansPostProcessor = beanProcessor.getBeansList();
    }

    /*5.1 BeanPostProcessor postProcessBeforeInitialization*/
    private void callPostProcessBeforeInitialization() {
        Invoker invoker = new PostProcessInvoker(systemBeansPostProcessor, beansList, "postProcessBeforeInitialization");
        invoker.invoke();
    }

    /*6. call init method(@PostProcess)*/
    private void callInitMethodOfBeansAnnotatedByPostProcess() {
        Invoker invoker = new PostConstructInvoker(beansList);
        invoker.invoke();
    }

    /*7. BeanPostProcessor postProcessAfterInitialization*/
    private void callPostProcessAfterInitialization() {
        Invoker invoker = new PostProcessInvoker(systemBeansPostProcessor, beansList, "postProcessAfterInitialization");
        invoker.invoke();
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
