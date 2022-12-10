package test;

import org.glassfish.jersey.test.JerseyTest;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.WebTarget;

public class JerseyTester implements TestRule {

	private final JerseyTest jerseyTest;

	public JerseyTester(JerseyTest jerseyTest) {
		this.jerseyTest = jerseyTest;
	}

	@Override
	public Statement apply(final Statement base, Description description) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				try {
					jerseyTest.setUp();
					base.evaluate();
				} finally {
					jerseyTest.tearDown();
				}
			}
		};
	}

	public WebTarget target() {
		return jerseyTest.target();
	}

	public WebTarget target(String path) {
		return jerseyTest.target(path);
	}

	public Client client() {
		return jerseyTest.client();
	}
}
