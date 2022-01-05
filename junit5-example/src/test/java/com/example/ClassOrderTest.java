package com.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
class ClassOrderTest {

	String value;

	@Order(1)
	@Nested
	class Foo {
		@Test
		void foo() {
			assertNull(value);
			value = "foo";
		}
	}

	@Order(2)
	@Nested
	class Bar {
		@Test
		void bar() {
			assertEquals("foo", value);
			value += ":bar";
		}
	}

	@Order(3)
	@Nested
	class Baz {
		@Test
		void baz() {
			assertEquals("foo:bar", value);
		}
	}
}
