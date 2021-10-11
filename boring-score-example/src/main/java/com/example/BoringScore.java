package com.example;

public class BoringScore {

	public static int[] calculateScore(int... pins) {
		int[] score = new int[10];
		for (int i = 0; i < 9; i++) {
			int a = pins[i * 2];
			int b = pins[i * 2 + 1];
			int c;
			if (a == 10) {
				int d = pins[i * 2 + 2];
				if (d == 10 && i != 8) {
					c = a + b + d + pins[i * 2 + 4];
				} else {
					c = a + b + d + pins[i * 2 + 3];
				}
			} else if (a + b == 10) {
				c = a + b + pins[i * 2 + 2];
			} else {
				c = a + b;
			}
			score[i] = c;
		}
		score[9] = pins[9 * 2] + pins[9 * 2 + 1] + pins[9 * 2 + 2];
		for (int i = 1; i < 10; i++) {
			score[i] += score[i - 1];
		}

		return score;
	}
}
