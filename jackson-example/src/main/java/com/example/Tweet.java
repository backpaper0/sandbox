package com.example;

import java.time.LocalDateTime;

public record Tweet(
		String content,
		@LocalDateTimeJacksonAnnotations LocalDateTime timestamp) {
}
