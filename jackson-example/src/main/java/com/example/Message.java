package com.example;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "content" })
public class Message {

    private final Integer id;
    private final String content;

    @JsonCreator
    public Message(@JsonProperty("id") final Integer id,
            @JsonProperty("content") final String content) {
        this.id = id;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        }
        final Message other = (Message) obj;
        return Objects.equals(id, other.id)
                && Objects.equals(content, other.content);
    }

    @Override
    public String toString() {
        return String.format("Message(%s, %s)", id, content);
    }
}
