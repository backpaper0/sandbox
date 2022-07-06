package com.example;

import org.junit.jupiter.api.Test;

public class NestedTest {

	// 実行される
	static class EeeTest {

		@Test
		void test() {
		}
	}

	// 実行される
	static class FffTest {

		@Test
		void test() {
		}
	}

	// 実行されない
	static class Ggg {

		@Test
		void test() {
		}
	}
}
