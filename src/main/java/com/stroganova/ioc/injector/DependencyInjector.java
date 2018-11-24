package com.stroganova.ioc.injector;

import com.stroganova.ioc.entity.Bean;
import com.stroganova.ioc.exception.BeanInitializationException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.util.Map;
import java.util.Set;

public class DependencyInjector extends Injector {

    public void inject(Bean bean, Map<String, String> dependencies) {
        Class<?> clazz = bean.getValue().getClass();
        Set<Map.Entry<String, String>> entries = dependencies.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            String propertyName = entry.getKey();
            String propertyValue = entry.getValue();
            try {
                Field field = clazz.getDeclaredField(propertyName);
                Class parameterType = field.getType();
                String methodName = getSetter(propertyName);
                Object arg = getArg(propertyValue, parameterType);
                Method method = clazz.getDeclaredMethod(methodName, parameterType);

                method.invoke(bean.getValue(), arg);
            } catch (NoSuchMethodException e) {
                throw new BeanInitializationException("There isn't a setter for \"" + propertyName + "\" field.", e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Object getArg(String value, Class<?> parameterType) {
        if (parameterType.isPrimitive()) {
            String typeName = parameterType.getTypeName();
            Object arg = null;
            if ("byte".equals(typeName)) {
                arg = Byte.valueOf(value);
            } else if ("short".equals(typeName)) {
                arg = Short.valueOf(value);
            } else if ("int".equals(typeName)) {
                arg = Integer.valueOf(value);
            } else if ("long".equals(typeName)) {
                arg = Long.valueOf(value);
            } else if ("float".equals(typeName)) {
                arg = Float.valueOf(value);
            } else if ("double".equals(typeName)) {
                arg = Double.valueOf(value);
            } else if ("char".equals(typeName)) {
                arg = value.charAt(0);
            } else if ("boolean".equals(typeName)) {
                arg = Boolean.valueOf(value);
            }
            return arg;
        } else {
            return value;
        }
    }


}









