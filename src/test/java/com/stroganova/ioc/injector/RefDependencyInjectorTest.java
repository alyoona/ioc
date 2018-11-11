package com.stroganova.ioc.injector;

import com.stroganova.ioc.entity.Bean;
import com.stroganova.ioc.exception.BeanInitializationException;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RefDependencyInjectorTest {


    @Test
    public void inject() throws Exception {

        class TheFirst {
        }
        class TheSecond {
            private TheFirst firstForSecond;

            public TheFirst getFirstForSecond() {
                return firstForSecond;
            }

            public void setFirstForSecond(TheFirst firstForSecond) {
                this.firstForSecond = firstForSecond;
            }
        }

        Bean beanFirst = new Bean();
        beanFirst.setValue(new TheFirst());

        Map<String, Bean> beans = new HashMap<>();
        beans.put("theFirst", beanFirst);

        Map<String, String> refDependencies = new HashMap<>();
        refDependencies.put("firstForSecond", "theFirst");

        TheSecond theSecond = new TheSecond();

        Bean beanSecond = new Bean();
        beanSecond.setValue(theSecond);

        RefDependencyInjector refDependencyInjector = new RefDependencyInjector();
        refDependencyInjector.inject(beanSecond, refDependencies, beans);

        Assert.assertTrue(theSecond.getFirstForSecond().getClass() == TheFirst.class);
        Assert.assertTrue(theSecond.getFirstForSecond() instanceof TheFirst);

    }

    @Test(expected = BeanInitializationException.class)
    public void testNoSetterInject() {
        class First {
        }
        class Second {
            private First firstForSecond;
        }

        Bean beanFirst = new Bean();
        beanFirst.setValue(new First());

        Map<String, Bean> beans = new HashMap<>();
        beans.put("first", beanFirst);

        Map<String, String> refDependencies = new HashMap<>();
        refDependencies.put("firstForSecond", "first");

        Second second = new Second();

        Bean beanSecond = new Bean();
        beanSecond.setValue(second);

        RefDependencyInjector refDependencyInjector = new RefDependencyInjector();
        refDependencyInjector.inject(beanSecond, refDependencies, beans);
    }

}