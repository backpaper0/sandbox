package com.example;

import javax.validation.constraints.NotNull;

public final class Bar {

    @NotNull
    private final String value;

    public Bar(final String value) {
        this.value = value;
    }
}
