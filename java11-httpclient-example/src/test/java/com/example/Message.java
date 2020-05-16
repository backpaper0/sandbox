package com.example;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Message {

	private final String message;

	@JsonCreator
	public Message(@JsonProperty("message") String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public int hashCode() {
		return Objects.hash(message);
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null
				&& obj.getClass() == getClass()
				&& message.equals(Message.class.cast(obj).message);
	}
}
