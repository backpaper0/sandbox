package parser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import parser.JsonParser.JsonException;

class JsonParserTest {

	@Test
	void test_object() throws Exception {
		var map = Map.class.cast(JsonParser.parse("{\"a\":\"hello\",\"b\":123,\"c\":true}"));
		assertEquals(3, map.size());
		assertEquals("hello", map.get("a"));
		assertEquals(123d, map.get("b"));
		assertEquals(true, map.get("c"));
	}

	@Test
	void test_object_with_whitespace() throws Exception {
		var map = Map.class
				.cast(JsonParser.parse("{ \"a\" : \"hello\", \"b\" : 123, \"c\" : true }"));
		assertEquals(3, map.size());
		assertEquals("hello", map.get("a"));
		assertEquals(123d, map.get("b"));
		assertEquals(true, map.get("c"));
	}

	@Test
	void test_nest() throws Exception {
		var map = Map.class.cast(JsonParser.parse("{ \"a\" : [\"b\", { \"c\" : null }] }"));
		assertEquals(1, map.size());
		var list = List.class.cast(map.get("a"));
		assertEquals(2, list.size());
		assertEquals("b", list.get(0));
		var map2 = Map.class.cast(list.get(1));
		assertEquals(1, map2.size());
		assertNull(map2.get("c"));
	}

	@Test
	void test_number_0() throws Exception {
		var map = Map.class.cast(JsonParser.parse("{\"a\":0}"));
		assertEquals(1, map.size());
		assertEquals(0d, map.get("a"));
	}

	@Test
	void test_number_negative() throws Exception {
		var map = Map.class.cast(JsonParser.parse("{\"a\":-456}"));
		assertEquals(1, map.size());
		assertEquals(-456d, map.get("a"));
	}

	@Test
	void test_number_point() throws Exception {
		var map = Map.class.cast(JsonParser.parse("{\"a\":12345.6789}"));
		assertEquals(1, map.size());
		assertEquals(12345.6789d, map.get("a"));
	}

	@Test
	void test_number_point_with_e() throws Exception {
		var map = Map.class.cast(JsonParser.parse("{\"a\":123e4}"));
		assertEquals(1, map.size());
		assertEquals(123e4d, map.get("a"));
	}

	@Test
	void test_number_point_with_e_with_sign() throws Exception {
		var map = Map.class.cast(JsonParser.parse("{\"a\":123e+4}"));
		assertEquals(1, map.size());
		assertEquals(123e+4d, map.get("a"));
	}

	@Test
	void test_number_point_with_E() throws Exception {
		var map = Map.class.cast(JsonParser.parse("{\"a\":567E-8}"));
		assertEquals(1, map.size());
		assertEquals(567E-8d, map.get("a"));
	}

	@Test
	void test_number_full() throws Exception {
		var map = Map.class.cast(JsonParser.parse("{\"a\":-123.456e-7}"));
		assertEquals(1, map.size());
		assertEquals(-123.456e-7d, map.get("a"));
	}

	@Test
	void test_string_contains_double_quote() throws Exception {
		var map = Map.class.cast(JsonParser.parse("{\"a\":\"\\\"\"}"));
		assertEquals(1, map.size());
		assertEquals("\"", map.get("a"));
	}

	@Test
	void test_string_unicode() throws Exception {
		var map = Map.class.cast(JsonParser.parse("{\"a\":\"\\u3042\"}"));
		assertEquals(1, map.size());
		assertEquals("あ", map.get("a"));
	}

	@Test
	void test_parse_error() throws Exception {
		assertThrows(JsonException.class,
				() -> JsonParser.parse("{\"a\":\"hello\",\"b\":123,\"c\":true}x"));
	}
}
