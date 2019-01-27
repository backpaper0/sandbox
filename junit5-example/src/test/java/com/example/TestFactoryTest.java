package com.example;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

class TestFactoryTest {

    @TestFactory
    Stream<DynamicTest> factory() {
        return Stream.of(
                DynamicTest.dynamicTest("foo", this::test),
                DynamicTest.dynamicTest("bar", this::test),
                DynamicTest.dynamicTest("baz", this::test));
    }

    private void test() {
    }
}
