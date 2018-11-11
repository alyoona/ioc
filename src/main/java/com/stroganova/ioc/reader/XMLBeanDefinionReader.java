package com.stroganova.ioc.reader;

import com.stroganova.ioc.entity.BeanDefinition;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLBeanDefinionReader implements BeanDefinitionReader {

    private String path;

    public List<BeanDefinition> readBeanDefinitions() {

        try (StaxProcessor xmlProcessor = new StaxProcessor(Files.newInputStream(Paths.get(path)))) {

            List<BeanDefinition> list = new ArrayList<>();

            while (xmlProcessor.startElement("bean", "beans")) {
                BeanDefinition beanDefinition = new BeanDefinition();
                beanDefinition.setId(xmlProcessor.getBeanId());
                beanDefinition.setBeanClassName(xmlProcessor.getBeanClass());


                while (xmlProcessor.startElement("property", "bean")) {
                    String key = xmlProcessor.getDependencyName();
                    String value;
                    if (xmlProcessor.isDependency()) {
                        value = xmlProcessor.getDependencyValue();
                        Map<String, String> dependencies;
                        dependencies = new HashMap<>();
                        dependencies.put(key, value);
                        beanDefinition.setDependencies(dependencies);
                    } else {
                        value = xmlProcessor.getDependencyRefValue();
                        Map<String, String> refDependencies;
                        refDependencies = new HashMap<>();
                        refDependencies.put(key, value);
                        beanDefinition.setRefDendencies(refDependencies);
                    }
                }
                list.add(beanDefinition);
            }

            return list;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void setPath(String path) {
        this.path = path;
    }


}
