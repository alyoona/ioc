package com.stroganova.ioc.exception;

public class NoUniqueBeanException extends RuntimeException {

    public NoUniqueBeanException(String message) {
        super(message);
    }

}
