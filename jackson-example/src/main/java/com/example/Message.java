package com.example;

public class Message {

    private final Integer id;
    private final String content;

    public Message(final Integer id, final String content) {
        this.id = id;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
