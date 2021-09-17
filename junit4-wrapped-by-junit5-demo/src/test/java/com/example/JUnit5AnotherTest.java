package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class JUnit5AnotherTest {

    @RegisterExtension
    static Wrapper wrapper = new Wrapper();

    @Test
    void test1() {
        System.out.println(getClass().getSimpleName() + ".test1");
    }

    @Test
    void test2() {
        System.out.println(getClass().getSimpleName() + ".test2");
    }
}
