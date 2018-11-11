package com.stroganova.ioc.entity;

import java.util.Map;

public class BeanDefinition {

    private String id;

    private String beanClassName;

    private Map<String, String> dependencies;

    private Map<String, String> refDendencies;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public Map<String, String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Map<String, String> dependencies) {
        this.dependencies = dependencies;
    }

    public Map<String, String> getRefDendencies() {
        return refDendencies;
    }

    public void setRefDendencies(Map<String, String> refDendencies) {
        this.refDendencies = refDendencies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BeanDefinition that = (BeanDefinition) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (beanClassName != null ? !beanClassName.equals(that.beanClassName) : that.beanClassName != null)
            return false;
        if (dependencies != null ? !dependencies.equals(that.dependencies) : that.dependencies != null) return false;
        return refDendencies != null ? refDendencies.equals(that.refDendencies) : that.refDendencies == null;
    }


}
