package com.example;

import java.lang.reflect.Method;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import org.junit.runners.model.Statement;

public class Wrapper implements BeforeEachCallback, AfterEachCallback, InvocationInterceptor {

    private AbstractTestByJUnit4 a = new AbstractTestByJUnit4() {
    };

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        a.destroy();
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        a.init();
    }

    @Override
    public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext,
            ExtensionContext extensionContext) throws Throwable {
        Statement statement = a.rule.apply(new Statement() {

            @Override
            public void evaluate() throws Throwable {
                invocation.proceed();
            }
        }, null);
        statement.evaluate();
    }
}
