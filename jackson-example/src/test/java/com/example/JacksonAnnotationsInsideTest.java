package com.example;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

class JacksonAnnotationsInsideTest {

	ObjectMapper om = new ObjectMapper();

	@Test
	void serialize() throws Exception {
		Tweet tweet = new Tweet("なるほど四時じゃねーの", LocalDateTime.parse("2022-01-02T04:00"));
		String json = om.writeValueAsString(tweet);
		JsonNode root = om.readTree(json);

		String timestamp = root.get("timestamp").asText();
		assertEquals("2022-01-02T04:00", timestamp);
	}

	@Test
	void deserialize() throws Exception {
		String json = """
				{
					"content": "なるほど四時じゃねーの",
					"timestamp": "2022-01-02T04:00"
				}
				""";
		Tweet tweet = om.readValue(json, Tweet.class);
		Tweet expected = new Tweet("なるほど四時じゃねーの", LocalDateTime.parse("2022-01-02T04:00"));
		assertEquals(expected, tweet);
	}
}
