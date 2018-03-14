package com.example;

import java.util.Objects;
import java.util.UUID;

public class Task {

    private final UUID id;
    private final String content;
    private final boolean done;

    private Task(final UUID id, final String content, final boolean done) {
        this.id = Objects.requireNonNull(id);
        this.content = Objects.requireNonNull(content);
        this.done = done;
    }

    public static Task create(final String content) {
        return new Task(UUID.randomUUID(), content, false);
    }
}
