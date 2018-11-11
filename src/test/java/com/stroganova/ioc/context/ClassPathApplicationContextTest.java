package com.stroganova.ioc.context;

import com.stroganova.ioc.exception.NoUniqueBeanException;
import com.stroganova.ioc.exception.NotFoundBeanException;
import com.stroganova.ioc.service.MailService;
import com.stroganova.ioc.service.SpamService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ClassPathApplicationContextTest {
    ClassPathApplicationContext context;

    @Before
    public void before() {
        context = new ClassPathApplicationContext("src/main/resources/context.xml");

    }

    @Test
    public void getBeanByClass() {
        SpamService expectedObject = new SpamService();
        expectedObject.setMailService(new MailService());
        expectedObject.setSpamCount(5);
        assertEquals(expectedObject, context.getBean(SpamService.class));
    }

    @Test
    public void getBeanByNameAndClass() {
        MailService expectedObject = new MailService();
        assertEquals(expectedObject, context.getBean("firstMailService", MailService.class));
    }

    @Test
    public void getBeanByName() throws Exception {
        Object expectedBeanObject = new MailService();
        assertEquals(expectedBeanObject, context.getBean("secondMailService"));
    }

    @Test
    public void getBeanNames() {
        List<String> actualBeanNames = context.getBeanNames();

        List<String> expectedBeanNames = new ArrayList<>();
        expectedBeanNames.add("secondMailService");
        expectedBeanNames.add("spamService");
        expectedBeanNames.add("firstMailService");
        assertEquals(actualBeanNames, expectedBeanNames);
    }

    @Test(expected = NoUniqueBeanException.class)
    public void getBeanByClassMoreThanOne() {
        context.getBean(MailService.class);
    }

    @Test(expected = NotFoundBeanException.class)
    public void getBeanByClassNotFound() {
        context.getBean(String.class);
    }

    @Test(expected = NotFoundBeanException.class)
    public void getBeanByNameAndClassNotFound() {
        context.getBean("firstMailService", SpamService.class);
    }

    @Test(expected = NotFoundBeanException.class)
    public void getBeanByNameNotFound() {
        context.getBean("notFound");
    }

}