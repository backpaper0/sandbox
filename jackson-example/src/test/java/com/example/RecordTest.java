package com.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RecordTest {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void serialize() throws Exception {
		Book book = new Book(
				new BookTitle("アオのハコ(2)"),
				new Author(
						new AuthorName("三浦 糀")));
		String json = objectMapper.writeValueAsString(book);
		String expected = """
				{
					"title": "アオのハコ(2)",
					"author": {
						"name": "三浦 糀"
					}
				}
				""";
		assertEquals(objectMapper.readTree(expected), objectMapper.readTree(json));
	}

	@Test
	void deserialize() throws Exception {
		String json = """
				{
					"title": "2.5次元の誘惑(11)",
					"author": {
						"name": "橋本 悠"
					}
				}
				""";
		Book book = objectMapper.readValue(json, Book.class);
		Book expected = new Book(
				new BookTitle("2.5次元の誘惑(11)"),
				new Author(
						new AuthorName("橋本 悠")));
		assertEquals(expected, book);
	}

	public record Book(BookTitle title, Author author) {
	}

	public record BookTitle(String value) {

		@JsonCreator(mode = Mode.DELEGATING)
		public BookTitle(String value) {
			this.value = value;
		}

		@JsonValue
		public String value() {
			return value;
		}
	}

	public record Author(AuthorName name) {
	}

	public record AuthorName(String value) {

		@JsonCreator(mode = Mode.DELEGATING)
		public AuthorName(String value) {
			this.value = value;
		}

		@JsonValue
		public String value() {
			return value;
		}
	}
}
