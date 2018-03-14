package com.example;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TaskList {

    private final List<Task> tasks;

    private TaskList(final List<Task> tasks) {
        this.tasks = Collections.unmodifiableList(tasks);
    }

    public static TaskList empty() {
        return new TaskList(Collections.emptyList());
    }

    public TaskList add(final Task addMe) {
        return new TaskList(
                Stream.concat(tasks.stream(), Stream.of(addMe)).collect(Collectors.toList()));
    }
}
