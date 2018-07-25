package com.example;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonTest {

    @Test
    public void test() throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        final String json = mapper.writeValueAsString(new Message(123, "foobar"));
        System.out.println(json);
        assertEquals("{\"id\":123,\"content\":\"foobar\"}", json);
    }
}
