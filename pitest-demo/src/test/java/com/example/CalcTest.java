package com.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CalcTest {

	private final Calc sut = new Calc();

	@Test
	void add() {
		int added = sut.add(2, 3);
		assertEquals(5, added);
	}

	@Test
	void sub() {
		// Calc.subを使っていないため、ミューテーション解析で指摘される
		assertEquals(-1, 2 - 3);
	}
}
