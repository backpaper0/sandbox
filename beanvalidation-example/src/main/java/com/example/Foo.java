package com.example;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public final class Foo {

    @NotNull
    private final String value;

    @Valid
    private final List<Bar> bars;

    public Foo(final String value, final List<Bar> bars) {
        this.value = value;
        this.bars = bars;
    }
}
