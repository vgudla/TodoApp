package com.example.vgudla.todoapp.persistence;

import java.io.Serializable;

/**
 * Model for a task
 */
public class TodoTask implements Serializable {
    private final String taskText;

    public TodoTask(String taskText) {
        this.taskText = taskText;
    }

    public String getTaskText() {
        return taskText;
    }
}
