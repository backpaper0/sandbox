package com.example;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class XmlTest {

    @Test
    public void test() throws Exception {
        final ObjectMapper mapper = new XmlMapper();
        final String json = mapper.writeValueAsString(new Message(123, "foobar"));
        System.out.println(json);
        assertEquals("<Message><id>123</id><content>foobar</content></Message>", json);
    }
}
