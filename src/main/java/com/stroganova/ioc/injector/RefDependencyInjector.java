package com.stroganova.ioc.injector;

import com.stroganova.ioc.entity.Bean;
import com.stroganova.ioc.exception.BeanInitializationException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class RefDependencyInjector extends Injector {

    public void inject(Bean bean, Map<String, String> refDependencies, Map<String, Bean> beans) {
        Class clazz = bean.getValue().getClass();
        Set<Map.Entry<String, String>> entries = refDependencies.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            String propertyName = entry.getKey();
            String propertyRefValue = entry.getValue();
            try {
                Field field = clazz.getDeclaredField(propertyName);
                Class parameterType = field.getType();
                String methodName = getSetter(propertyName);
                Bean refBean = beans.get(propertyRefValue);
                Method method = clazz.getDeclaredMethod(methodName, parameterType);
                method.invoke(bean.getValue(), refBean.getValue());
            } catch (NoSuchMethodException e) {
                throw new BeanInitializationException("There isn't a setter for \"" + propertyName + "\" field.", e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
