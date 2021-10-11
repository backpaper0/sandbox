package com.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BoringScoreTest {

	@Test
	void test() {
		int[] pins = {
				6, 3,
				9, 0,
				0, 3,
				8, 2,
				7, 3,
				10, 0,
				9, 1,
				8, 0,
				10, 0,
				10, 6, 4
		};
		int[] score = BoringScore.calculateScore(pins);
		int[] expected = { 9, 18, 21, 38, 58, 78, 96, 104, 130, 150 };
		assertArrayEquals(expected, score);
	}
}
