package com.stroganova.ioc.processor;

import com.stroganova.ioc.entity.BeanDefinition;
import com.stroganova.ioc.entity.BeanType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.stroganova.ioc.processor.TestUtil.getBeanDefinition;
import static org.junit.Assert.*;

public class BeanDefinitionProcessorTest {

    @Test
    public void getAllBeanDefinitionsMap() throws Exception {

        List<BeanDefinition> beanDefinitions = new ArrayList<>();
        List<BeanDefinition> beanBFPPDefinitions = new ArrayList<>();
        List<BeanDefinition> beanBPPDefinitions = new ArrayList<>();

        beanBFPPDefinitions.add(getBeanDefinition("BFPPClassOne", "com.stroganova.ioc.service.BFPPClassOne", null,null));
        beanBFPPDefinitions.add(getBeanDefinition("BFPPClassTwo", "com.stroganova.ioc.service.BFPPClassTwo", null,null));

        beanBPPDefinitions.add(getBeanDefinition("BPPClassOne", "com.stroganova.ioc.service.BPPClassOne", null,null));
        beanBPPDefinitions.add(getBeanDefinition("BPPClassTwo", "com.stroganova.ioc.service.BPPClassTwo", null,null));

        beanDefinitions.add(getBeanDefinition("PostConstructService", "com.stroganova.ioc.service.PostConstructService", null,null));
        beanDefinitions.add(getBeanDefinition("MailService", "com.stroganova.ioc.service.MailService", null,null));
        beanDefinitions.add(getBeanDefinition("SpamService", "com.stroganova.ioc.service.SpamService", null,null));

        List<BeanDefinition> allBeanDefinitions = new ArrayList<>();
        allBeanDefinitions.addAll(beanDefinitions);
        allBeanDefinitions.addAll(beanBFPPDefinitions);
        allBeanDefinitions.addAll(beanBPPDefinitions);

        BeanDefinitionProcessor beanDefinitionProcessor = new BeanDefinitionProcessor(allBeanDefinitions);

        Map<BeanType, List<BeanDefinition>> map = beanDefinitionProcessor.getAllBeanDefinitionsMap();

        List<BeanDefinition> beanDefinitionsProcessed = map.get(BeanType.BUSINESS);
        List<BeanDefinition> beanBFPPDefinitionsProcessed = map.get(BeanType.BEAN_FACTORY_POST_PROCESSOR);
        List<BeanDefinition> beanBPPDefinitionsProcessed = map.get(BeanType.BEAN_POST_PROCESSOR);

        assertEquals(beanDefinitions, beanDefinitionsProcessed);
        assertEquals(beanBFPPDefinitions, beanBFPPDefinitionsProcessed);
        assertEquals(beanBPPDefinitions, beanBPPDefinitionsProcessed);
    }




}