package com.example;

import static org.junit.jupiter.api.Assertions.*;

import java.io.StringWriter;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;

public class JsonTest {

	@Test
	public void testWrite() throws Exception {
		final ObjectMapper mapper = new ObjectMapper();
		final String json = mapper.writeValueAsString(new Message(123, "foobar"));
		System.out.println(json);
		assertEquals("{\"id\":123,\"content\":\"foobar\"}", json);
	}

	@Test
	public void testWriteValues() throws Exception {
		final ObjectMapper mapper = new ObjectMapper();

		final StringWriter out = new StringWriter();
		try (final SequenceWriter writer = mapper.writerFor(Message.class).writeValues(out)) {
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

	@Test
	public void testRead() throws Exception {
		final ObjectMapper mapper = new ObjectMapper();

		final String json = "{\"id\":123,\"content\":\"foobar\"}";
		final Message message = mapper.readValue(json, Message.class);
		System.out.println(message);
		assertEquals(new Message(123, "foobar"), message);
	}

	@Test
	public void testReadValues() throws Exception {
		final ObjectMapper mapper = new ObjectMapper();

		final String json = "[{\"id\":123,\"content\":\"foo\"},{\"id\":456,\"content\":\"bar\"},{\"id\":789,\"content\":\"baz\"}]";
		try (MappingIterator<Message> iterator = mapper.readerFor(Message.class).readValues(json)) {
			final Message message1 = iterator.next();
			final Message message2 = iterator.next();
			final Message message3 = iterator.next();
			System.out.println(message1);
			System.out.println(message2);
			System.out.println(message3);
			assertEquals(new Message(123, "foo"), message1);
			assertEquals(new Message(456, "bar"), message2);
			assertEquals(new Message(789, "baz"), message3);
			assertFalse(iterator.hasNext());
		}
	}
}
