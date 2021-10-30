package com.example.bar;

import com.example.baz.Baz;

public class Bar {
    @Override
    public String toString() {
        return "Bar, " + new Baz();
    }
}
