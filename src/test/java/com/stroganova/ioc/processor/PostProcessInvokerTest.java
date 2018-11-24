package com.stroganova.ioc.processor;

import com.stroganova.ioc.entity.Bean;
import com.stroganova.ioc.service.BPPClassOne;
import com.stroganova.ioc.service.BPPClassTwo;
import com.stroganova.ioc.service.SomeService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static com.stroganova.ioc.processor.TestUtil.getBean;

import static org.junit.Assert.*;

public class PostProcessInvokerTest {

    private List<Bean> systemBeans;
    private List<Bean> beans;


    @Before
    public void before() {
        systemBeans = new ArrayList<>();
        systemBeans.add(getBean(new BPPClassOne(), "BPPClassOne"));
        systemBeans.add(getBean(new BPPClassTwo(), "BPPClassTwo"));
        beans = new ArrayList<>();
        beans.add(getBean(new SomeService(), "someService"));

    }

    @Test
    public void invokeBefore() throws Exception {
        PostProcessInvoker invoker = new PostProcessInvoker(systemBeans, beans, "postProcessBeforeInitialization");
        invoker.invoke();

        Bean bean = beans.get(0);
        Object value = bean.getValue();
        assertEquals(ArrayList.class, value.getClass());
    }

    @Test
    public void invokeAfter() throws Exception {
        PostProcessInvoker invoker = new PostProcessInvoker(systemBeans, beans, "postProcessAfterInitialization");
        invoker.invoke();

        Bean bean = beans.get(0);
        Object value = bean.getValue();
        assertEquals(TreeSet.class, value.getClass());

    }

}