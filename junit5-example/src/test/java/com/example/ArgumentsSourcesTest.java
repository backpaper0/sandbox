package com.example;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

class ArgumentsSourcesTest {

    @ParameterizedTest
    @ArgumentsSource(Foo.class)
    @ArgumentsSource(Bar.class)
    void test(final Object a) {
        System.out.println(a);
    }

    static class Foo implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(final ExtensionContext context)
                throws Exception {
            return Stream.of(1, 2, 3).map(Arguments::of);
        }
    }

    static class Bar implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(final ExtensionContext context)
                throws Exception {
            return Stream.of(4, 5).map(Arguments::of);
        }
    }
}
