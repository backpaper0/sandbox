package com.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class HelloServiceTest {

	HelloService sut = new HelloService();

	@Test
	void test() {
		String actual = sut.sayHello("World");
		assertEquals("Hello World", actual);
	}
}
