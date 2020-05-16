package com.example;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodySubscribers;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonBodyHandler {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static <T> HttpResponse.BodyHandler<T> of(Class<T> clazz) {
		return responseInfo -> BodySubscribers.mapping(BodySubscribers.ofByteArray(),
				bytes -> {
					try {
						return objectMapper.readerFor(clazz).readValue(bytes);
					} catch (final IOException e) {
						throw new UncheckedIOException(e);
					}
				});
	}
}
