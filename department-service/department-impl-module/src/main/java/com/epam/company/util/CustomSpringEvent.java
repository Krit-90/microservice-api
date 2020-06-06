package com.epam.company.util;

public class CustomSpringEvent<T> {
    private T source;

    public CustomSpringEvent(T source) {
        this.source = source;
    }

    public T getSource() {
        return source;
    }

    public void setSource(T source) {
        this.source = source;
    }
}
