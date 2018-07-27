package com.example;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class XmlTest {

    @Test
    public void testWrite() throws Exception {
        final ObjectMapper mapper = new XmlMapper();

        final String xml = mapper.writeValueAsString(new Message(123, "foobar"));
        System.out.println(xml);
        assertEquals("<Message><id>123</id><content>foobar</content></Message>", xml);
    }

    @Test
    public void testRead() throws Exception {
        final ObjectMapper mapper = new XmlMapper();

        final String xml = "<Message><id>123</id><content>foobar</content></Message>";
        final Message message = mapper.readValue(xml, Message.class);
        System.out.println(message);
        assertEquals(new Message(123, "foobar"), message);
    }
}
