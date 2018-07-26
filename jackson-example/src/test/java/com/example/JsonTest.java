package com.example;

import static org.junit.Assert.*;

import java.io.StringWriter;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;

public class JsonTest {

    @Test
    public void test() throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        final String json = mapper.writeValueAsString(new Message(123, "foobar"));
        System.out.println(json);
        assertEquals("{\"id\":123,\"content\":\"foobar\"}", json);
    }

    @Test
    public void testIteration() throws Exception {
        final ObjectMapper mapper = new ObjectMapper();

        final StringWriter out = new StringWriter();
        try (final SequenceWriter writer = mapper.writer().writeValues(out)) {
            writer.init(true);
            writer.write(new Message(123, "foo"));
            writer.write(new Message(456, "bar"));
            writer.write(new Message(789, "baz"));
        }

        final String json = out.toString();
        System.out.println(json);
        assertEquals(
                "[{\"id\":123,\"content\":\"foo\"},{\"id\":456,\"content\":\"bar\"},{\"id\":789,\"content\":\"baz\"}]",
                json);
    }
}
