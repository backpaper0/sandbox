package com.example;

import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonBodyPublisher {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static BodyPublisher of(Object value) throws JsonProcessingException {
		final String body = objectMapper.writeValueAsString(value);
		return BodyPublishers.ofString(body);
	}
}
