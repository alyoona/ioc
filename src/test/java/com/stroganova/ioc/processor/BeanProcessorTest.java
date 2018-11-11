package com.stroganova.ioc.processor;

import com.stroganova.ioc.entity.Bean;
import com.stroganova.ioc.entity.BeanDefinition;
import com.stroganova.ioc.service.MailService;
import com.stroganova.ioc.service.SpamService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BeanProcessorTest {

    private List<BeanDefinition> beanDefinitionsList;

    @Before
    public void before() {
        BeanDefinition beanDefinitionFirst = new BeanDefinition();
        beanDefinitionFirst.setId("firstMailService");
        beanDefinitionFirst.setBeanClassName("com.stroganova.ioc.service.MailService");

        BeanDefinition beanDefinitionSecond = new BeanDefinition();
        beanDefinitionSecond.setId("secondMailService");
        beanDefinitionSecond.setBeanClassName("com.stroganova.ioc.service.MailService");

        BeanDefinition beanDefinitionSpam = new BeanDefinition();
        beanDefinitionSpam.setId("spamService");
        beanDefinitionSpam.setBeanClassName("com.stroganova.ioc.service.SpamService");

        beanDefinitionsList = new ArrayList<>();
        beanDefinitionsList.add(beanDefinitionFirst);
        beanDefinitionsList.add(beanDefinitionSecond);
        beanDefinitionsList.add(beanDefinitionSpam);
    }

    @Test
    public void process() throws Exception {
        //prepare
        Map<String, Bean> expectedBeans = new HashMap<>();
        MailService firstMailService = new MailService();
        MailService secondMailService = new MailService();
        SpamService spamService = new SpamService();

        Bean beanFirstMailService = new Bean();
        beanFirstMailService.setId("firstMailService");
        beanFirstMailService.setValue(firstMailService);
        Bean beanSecondMailService = new Bean();
        beanSecondMailService.setId("secondMailService");
        beanSecondMailService.setValue(secondMailService);
        Bean beanSpamService = new Bean();
        beanSpamService.setId("spamService");
        beanSpamService.setValue(spamService);

        expectedBeans.put("firstMailService", beanFirstMailService);
        expectedBeans.put("secondMailService", beanSecondMailService);
        expectedBeans.put("spamService", beanSpamService);
        //when
        BeanProcessor beanProcessor = new BeanProcessor();
        beanProcessor.process(beanDefinitionsList);

        Map<String, Bean> actualBeans = beanProcessor.getBeans();

        //then
        assertEquals("Size mismatch for maps;", expectedBeans.size(), actualBeans.size());
        Assert.assertTrue("Missing keys in actual map;", actualBeans.keySet().containsAll(expectedBeans.keySet()));
        expectedBeans.keySet().stream().forEach((key) -> {
            assertEquals("Value mismatch for key '" + key + "';", expectedBeans.get(key), actualBeans.get(key));
        });

        Map<Class, Integer> mapCount = beanProcessor.getBeansCountByType();
        assertEquals(Integer.valueOf(2), mapCount.get(MailService.class));
        assertEquals(Integer.valueOf(1), mapCount.get(SpamService.class));

        Map<Class, String> mapByType = beanProcessor.getBeansIdByType();
        assertEquals(null, mapByType.get(MailService.class));
        assertEquals("spamService", mapByType.get(SpamService.class));

    }


}