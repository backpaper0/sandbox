package com.example;

import org.junit.jupiter.api.Test;

// 実行される
public class AaaTest {

	@Test
	void test() {
	}

	// 実行されない
	static class AaaDto {
		String content;
	}
}
