package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(Wrapper.class)
public class JUnit5Test {

    @Test
    void test1() {
        System.out.println(getClass().getSimpleName() + ".test1");
    }

    @Test
    void test2() {
        System.out.println(getClass().getSimpleName() + ".test2");
    }
}
