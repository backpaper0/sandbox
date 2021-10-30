package com.example.foo;

import com.example.bar.Bar;
import com.example.baz.Baz;

public class Foo {
    public static void main(String[] args) {
        System.out.println("Foo, " + new Bar() + ", " + new Baz());
    }
}
