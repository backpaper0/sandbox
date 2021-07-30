package com.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class FoobarTest {

    @Test
    void test() {
        Foobar sut = new Foobar("foo", "bar");
        assertEquals("foobar", sut.foobar());
    }
}
