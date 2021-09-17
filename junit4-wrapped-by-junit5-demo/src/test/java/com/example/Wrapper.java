package com.example;

import java.lang.reflect.Method;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import org.junit.runners.model.Statement;

public class Wrapper implements InvocationInterceptor, BeforeAllCallback, AfterAllCallback {

    private final AbstractTestByJUnit4 base = new AbstractTestByJUnit4() {
    };

    @Override
    public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext,
            ExtensionContext extensionContext) throws Throwable {
        Statement statement = base.rule.apply(new Statement() {

            @Override
            public void evaluate() throws Throwable {
                base.init();
                try {
                    invocation.proceed();
                } finally {
                    base.destroy();
                }
            }
        }, null);
        statement.evaluate();
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        AbstractTestByJUnit4.initStatic();
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        AbstractTestByJUnit4.destroyStatic();
    }
}
