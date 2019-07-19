package com.example;

import java.io.Serializable;

public class Foo implements Serializable {

    private static final long serialVersionUID = 1L;
    private final String message;

    public Foo(final String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return String.format("Foo(%s)", message);
    }
}
