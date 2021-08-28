package com.example;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;
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

	@Test
	public void testReadMessages() throws Exception {
		final ObjectMapper mapper = new XmlMapper();

		final String xml = "<Messages>"
				+ "<Message><id>1</id><content>foo</content></Message>"
				+ "<Message><id>2</id><content>bar</content></Message>"
				+ "<Message><id>3</id><content>baz</content></Message>"
				+ "</Messages>";
		final TypeReference<List<Message>> ref = new TypeReference<>() {
		};
		final List<Message> messages = mapper.readValue(xml, ref);
		System.out.println(messages);
		assertEquals(List.of(new Message(1, "foo"), new Message(2, "bar"), new Message(3, "baz")),
				messages);
	}
}
