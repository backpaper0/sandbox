package com.example;

import org.junit.Test;

public class JUnit4Test extends AbstractTestByJUnit4 {

    @Test
    public void test1() {
        System.out.println(getClass().getSimpleName() + ".test1");
    }

    @Test
    public void test2() {
        System.out.println(getClass().getSimpleName() + ".test2");
    }
}
