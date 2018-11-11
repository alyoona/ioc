package com.stroganova.ioc.injector;

class Injector {

    String getSetter(String name) {
        return "set" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
    }
}
