package com.stroganova.ioc.processor;

import com.stroganova.ioc.entity.Bean;
import com.stroganova.ioc.service.BFPPClassOne;
import com.stroganova.ioc.service.BFPPClassTwo;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.stroganova.ioc.processor.TestUtil.getBean;
import static org.junit.Assert.*;

public class PostProcessBeanFactoryInvokerTest {
    @Test
    public void invoke() throws Exception {

        BFPPClassOne one = new BFPPClassOne();
        BFPPClassTwo two = new BFPPClassTwo();

        List<Bean> systemBeans = new ArrayList<>();
        systemBeans.add(getBean(one, "BFPPClassOne"));
        systemBeans.add(getBean(two, "BFPPClassTwo"));

        PostProcessBeanFactoryInvoker invoker = new PostProcessBeanFactoryInvoker(systemBeans, new ArrayList<>());

        invoker.invoke();

        assertEquals("definitions have been processed by BFPPClassOne before creating beans", one.getStatus());
        assertEquals("definitions have been processed by BFPPClassTwo before creating beans", two.getStatus());

    }
}