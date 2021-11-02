package com.example.foo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FooTest {
	@Test
	public void bar() {
		assertNotNull(new com.example.bar.Bar());
	}

	@Test
	public void internalBar() {
		assertNotNull(new com.example.bar.internal.InternalBar());
	}
}
