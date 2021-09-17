package com.example;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public abstract class AbstractTestByJUnit4 {

    @Rule
    public DemoTestRule rule = new DemoTestRule();

    @Before
    public void init() {
        System.out.println("AbstractTestByJUnit4.init");
    }

    @After
    public void destroy() {
        System.out.println("AbstractTestByJUnit4.destroy");
    }

    public static class DemoTestRule implements TestRule {

        @Override
        public Statement apply(Statement base, Description description) {
            return new Statement() {

                @Override
                public void evaluate() throws Throwable {
                    System.out.println("DemoTestRule BEGIN");
                    base.evaluate();
                    System.out.println("DemoTestRule END");
                }
            };
        }
    }
}
