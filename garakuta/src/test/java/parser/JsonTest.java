package parser;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class JsonTest {

    @Test
    public void test_object() throws Exception {
        Map<String, Object> map =
            (Map<String, Object>) Json.get("{\"a\":\"hello\",\"b\":123,\"c\":true}");
        assertEquals(3, map.size());
        assertEquals("hello", map.get("a"));
        assertEquals(123d, map.get("b"));
        assertEquals(true, map.get("c"));
    }

    @Test
    public void test_object_with_whitespace() throws Exception {
        Map<String, Object> map =
            (Map<String, Object>) Json.get("{ \"a\" : \"hello\", \"b\" : 123, \"c\" : true }");
        assertEquals(3, map.size());
        assertEquals("hello", map.get("a"));
        assertEquals(123d, map.get("b"));
        assertEquals(true, map.get("c"));
    }

    @Test
    public void test_nest() throws Exception {
        Map<String, Object> map =
            (Map<String, Object>) Json.get("{ \"a\" : [\"b\", { \"c\" : null }] }");
        assertEquals(1, map.size());
        List<Object> list = (List<Object>) map.get("a");
        assertEquals(2, list.size());
        assertEquals("b", list.get(0));
        Map<String, Object> map2 = (Map<String, Object>) list.get(1);
        assertEquals(1, map2.size());
        assertNull(map2.get("c"));
    }

    @Test
    public void test_number_0() throws Exception {
        Map<String, Object> map = (Map<String, Object>) Json.get("{\"a\":0}");
        assertEquals(1, map.size());
        assertEquals(0d, map.get("a"));
    }

    @Test
    public void test_number_negative() throws Exception {
        Map<String, Object> map = (Map<String, Object>) Json.get("{\"a\":-456}");
        assertEquals(1, map.size());
        assertEquals(-456d, map.get("a"));
    }

    @Test
    public void test_number_point() throws Exception {
        Map<String, Object> map = (Map<String, Object>) Json.get("{\"a\":12345.6789}");
        assertEquals(1, map.size());
        assertEquals(12345.6789d, map.get("a"));
    }

    @Test
    public void test_number_point_with_e() throws Exception {
        Map<String, Object> map = (Map<String, Object>) Json.get("{\"a\":123e4}");
        assertEquals(1, map.size());
        assertEquals(123e4d, map.get("a"));
    }

    @Test
    public void test_number_point_with_e_with_sign() throws Exception {
        Map<String, Object> map = (Map<String, Object>) Json.get("{\"a\":123e+4}");
        assertEquals(1, map.size());
        assertEquals(123e+4d, map.get("a"));
    }

    @Test
    public void test_number_point_with_E() throws Exception {
        Map<String, Object> map = (Map<String, Object>) Json.get("{\"a\":567E-8}");
        assertEquals(1, map.size());
        assertEquals(567E-8d, map.get("a"));
    }

    @Test
    public void test_number_full() throws Exception {
        Map<String, Object> map = (Map<String, Object>) Json.get("{\"a\":-123.456e-7}");
        assertEquals(1, map.size());
        assertEquals(-123.456e-7d, map.get("a"));
    }

    @Test
    public void test_string_contains_double_quote() throws Exception {
        Map<String, Object> map = (Map<String, Object>) Json.get("{\"a\":\"\\\"\"}");
        assertEquals(1, map.size());
        assertEquals("\"", map.get("a"));
    }

    @Test
    public void test_string_unicode() throws Exception {
        Map<String, Object> map = (Map<String, Object>) Json.get("{\"a\":\"\\u3042\"}");
        assertEquals(1, map.size());
        assertEquals("あ", map.get("a"));
    }

}
