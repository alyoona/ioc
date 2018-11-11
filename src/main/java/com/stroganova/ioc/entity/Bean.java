package com.stroganova.ioc.entity;

public class Bean {
    private Object value;
    private String id;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bean bean = (Bean) o;

        if (value != null ? !value.equals(bean.value) : bean.value != null) return false;
        return id != null ? id.equals(bean.id) : bean.id == null;
    }


}
