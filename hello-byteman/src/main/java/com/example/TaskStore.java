package com.example;

public class TaskStore {

    private TaskList taskList = TaskList.empty();

    public void addTask(final String content) {
        final Task addMe = Task.create(content);
        taskList = taskList.add(addMe);
    }
}
