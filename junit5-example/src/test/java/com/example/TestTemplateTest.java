package com.example;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;

class TestTemplateTest {

    @TestTemplate
    @ExtendWith(Foo.class)
    void test(final String a) {
        assertEquals("hello world", a);
    }

    static class Foo implements TestTemplateInvocationContextProvider {

        @Override
        public boolean supportsTestTemplate(final ExtensionContext context) {
            return true;
        }

        @Override
        public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(
                final ExtensionContext context) {
            return Stream.of(new TestTemplateInvocationContext() {
                @Override
                public List<Extension> getAdditionalExtensions() {
                    return List.of(new ParameterResolver() {

                        @Override
                        public boolean supportsParameter(final ParameterContext parameterContext,
                                final ExtensionContext extensionContext)
                                throws ParameterResolutionException {
                            return true;
                        }

                        @Override
                        public Object resolveParameter(final ParameterContext parameterContext,
                                final ExtensionContext extensionContext)
                                throws ParameterResolutionException {
                            return "hello world";
                        }
                    });
                }
            });
        }
    }
}
