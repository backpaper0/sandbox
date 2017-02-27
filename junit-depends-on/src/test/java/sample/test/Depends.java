package sample.test;

import org.junit.Assume;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class Depends implements TestRule {
    private static boolean failed;
    @Override
    public Statement apply(Statement base, Description description) {
        if (failed) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    Assume.assumeTrue(false);
                }
            };
        }
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    base.evaluate();
                } catch (Throwable t) {
                    failed = true;
                    throw t;
                }
            }
        };
    }
}
