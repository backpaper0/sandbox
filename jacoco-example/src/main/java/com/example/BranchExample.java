package com.example;

import java.util.function.BooleanSupplier;

public class BranchExample {

	public String example1(BooleanSupplier condition) {
		if (condition.getAsBoolean()) {
			return "yes";
		}
		return "no";
	}

	public String example2(BooleanSupplier condition1, BooleanSupplier condition2) {
		if (condition1.getAsBoolean() && condition2.getAsBoolean()) {
			return "yes";
		}
		return "no";
	}

	public String example3(BooleanSupplier condition1, BooleanSupplier condition2) {
		if (condition1.getAsBoolean() || condition2.getAsBoolean()) {
			return "yes";
		}
		return "no";
	}
}
