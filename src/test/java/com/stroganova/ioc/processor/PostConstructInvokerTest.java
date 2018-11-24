package com.stroganova.ioc.processor;

import com.stroganova.ioc.entity.Bean;
import com.stroganova.ioc.service.PostConstructService;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PostConstructInvokerTest {


    @Test
    public void invoke() throws Exception {

        List<Bean> beans = new ArrayList<>();

        Bean bean = new Bean();
        bean.setId("postConstructService");
        bean.setValue(new PostConstructService());

        beans.add(bean);

        PostConstructInvoker invoker = new PostConstructInvoker(beans);

        invoker.invoke();

        PostConstructService postConstructService = (PostConstructService) bean.getValue();

        assertEquals("init method has been called", postConstructService.getStatus());

    }

}