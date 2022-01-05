package com.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
class MethodOrderTest {

	String value;

	@Order(1)
	@Test
	void foo() {
		assertNull(value);
		value = "foo";
	}

	@Order(2)
	@Test
	void bar() {
		assertEquals("foo", value);
		value += ":bar";
	}

	@Order(3)
	@Test
	void baz() {
		assertEquals("foo:bar", value);
	}
}
