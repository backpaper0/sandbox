package com.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class BranchExampleTest {

	BranchExample sut = new BranchExample();

	@ParameterizedTest
	@CsvSource(delimiter = '|', value = {
			" true  | yes ",
			" false | no  ",
	})
	void testExample1(boolean input, String expected) {
		String actual = sut.example1(() -> input);
		assertEquals(expected, actual);
	}

	@ParameterizedTest
	@CsvSource(delimiter = '|', value = {
			" true  | true  | yes ",
			" true  | false | no  ",
			" false | true  | no  ",
	//			" false | false | no  ",
	})
	void testExample2(boolean input1, boolean input2, String expected) {
		String actual = sut.example2(() -> input1, () -> input2);
		assertEquals(expected, actual);
	}

	@ParameterizedTest
	@CsvSource(delimiter = '|', value = {
			" true  | true  | yes ",
			//			" true  | false | yes ",
			" false | true  | yes ",
			" false | false | no  ",
	})
	void testExample3(boolean input1, boolean input2, String expected) {
		String actual = sut.example3(() -> input1, () -> input2);
		assertEquals(expected, actual);
	}
}
