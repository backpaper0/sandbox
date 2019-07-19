package com.example;

import java.io.Serializable;

public class Foo implements Serializable {

    private static final long serialVersionUID = 1L;
    private final String message;
    private final int count;

    public Foo(final String message, final int count) {
        this.message = message;
        this.count = count;
    }

    @Override
    public String toString() {
        return String.format("Foo(%s, %s)", message, count);
    }
}
