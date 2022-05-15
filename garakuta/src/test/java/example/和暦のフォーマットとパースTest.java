package example;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import example.和暦.和暦のフォーマットとパース;
import example.和暦.和暦のフォーマットとパース.Style;

public class 和暦のフォーマットとパースTest {

	static class 和暦のパースTest {

		@ParameterizedTest
		@CsvSource(delimiter = '|', value = {
				"M06/01/01 | 1873-01-01",
				"M45/07/29 | 1912-07-29",
				"T01/07/30 | 1912-07-30",
				"T15/12/24 | 1926-12-24",
				"S01/12/25 | 1926-12-25",
				"S64/01/07 | 1989-01-07",
				"H01/01/08 | 1989-01-08",
				"H31/04/30 | 2019-04-30",
				"R01/05/01 | 2019-05-01",
				"R04/05/15 | 2022-05-15",
		})
		void 正常処理(String input, String expected) {
			LocalDate actual = 和暦のフォーマットとパース.和暦のパース(input);
			assertEquals(LocalDate.parse(expected), actual);
		}

		@ParameterizedTest
		@CsvSource(delimiter = '|', value = {
				"M05/12/31",
				"M45/07/30",
				"T01/07/29",
				"T15/12/25",
				"S01/12/24",
				"S64/01/08",
				"H01/01/07",
				"H31/05/01",
				"R01/04/30",
		})
		void 不正な和暦(String input) {
			LocalDate actual = 和暦のフォーマットとパース.和暦のパース(input);
			assertNull(actual);
		}

		@ParameterizedTest
		@CsvSource(delimiter = '|', value = {
				"X01/02/03",
		})
		void 存在しない元号(String input) {
			LocalDate actual = 和暦のフォーマットとパース.和暦のパース(input);
			assertNull(actual);
		}
	}

	static class 和暦へ変換Test {

		@ParameterizedTest
		@CsvSource(delimiter = '|', value = {
				"1873-01-01 | 明治06年01月01日",
				"1912-07-29 | 明治45年07月29日",
				"1912-07-30 | 大正01年07月30日",
				"1926-12-24 | 大正15年12月24日",
				"1926-12-25 | 昭和01年12月25日",
				"1989-01-07 | 昭和64年01月07日",
				"1989-01-08 | 平成01年01月08日",
				"2019-04-30 | 平成31年04月30日",
				"2019-05-01 | 令和01年05月01日",
				"2022-05-15 | 令和04年05月15日",
		})
		void スタイル1(String input, String expected) {
			String actual = 和暦のフォーマットとパース.和暦へ変換(LocalDate.parse(input), Style._1);
			assertEquals(expected, actual);
		}

		@ParameterizedTest
		@CsvSource(delimiter = '|', value = {
				"1873-01-01 | M06/01/01",
				"1912-07-29 | M45/07/29",
				"1912-07-30 | T01/07/30",
				"1926-12-24 | T15/12/24",
				"1926-12-25 | S01/12/25",
				"1989-01-07 | S64/01/07",
				"1989-01-08 | H01/01/08",
				"2019-04-30 | H31/04/30",
				"2019-05-01 | R01/05/01",
				"2022-05-15 | R04/05/15",
		})
		void スタイル2(String input, String expected) {
			String actual = 和暦のフォーマットとパース.和暦へ変換(LocalDate.parse(input), Style._2);
			assertEquals(expected, actual);
		}

		@Test
		void 明治6年1月1日以前はサポートされない() {
			String actual = 和暦のフォーマットとパース.和暦へ変換(LocalDate.parse("1872-12-31"), Style._1);
			assertNull(actual);
		}
	}
}
