package com.example.vgudla.todoapp.persistence;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Model for a task
 */
public class TodoTask implements Serializable {
    public enum Priority {
        LOW,
        MEDIUM,
        HIGH
    }

    private String taskText;
    private Priority taskPriority;
    private DateTime taskCompletionDate;
    private static final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

    public TodoTask(String taskText) {
        this.taskText = taskText;
        this.taskPriority = Priority.LOW;
        this.taskCompletionDate = new DateTime();
    }

    public TodoTask(String taskText, String priority, String date) {
        this.taskText = taskText;
        this.taskPriority = Priority.valueOf(priority);
        this.taskCompletionDate = formatter.parseDateTime(date);
    }

    public TodoTask(JSONObject object){
        try {
            this.taskText = object.getString("taskText");
            this.taskPriority = Priority.valueOf(object.getString("taskPriority"));
            this.taskCompletionDate = formatter.parseDateTime(object.getString("taskCompletionDate"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public TodoTask(TodoTask other) {
        this.taskText = other.taskText;
        this.taskCompletionDate = other.taskCompletionDate;
        this.taskPriority = other.taskPriority;
    }

    public static List<TodoTask> fromJson(JSONArray jsonObjects) {
        List<TodoTask> tasks = new ArrayList<>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                tasks.add(new TodoTask(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tasks;
    }


    public String getTaskText() {
        return taskText;
    }

    public String getTaskPriority() {
        return taskPriority.toString();
    }

    public String getTaskCompletionDate() {
        return formatter.print(taskCompletionDate);
    }

    public void setTaskText(String taskText) {
        this.taskText = taskText;
    }

    public void setTaskPriority(Priority taskPriority) {
        this.taskPriority = taskPriority;
    }

    public void setTaskCompletionDate(String taskCompletionDate) {
        this.taskCompletionDate = formatter.parseDateTime(taskCompletionDate);
    }

    public void setTaskCompletionDate(DateTime dateTime) {
        this.taskCompletionDate = dateTime;
    }
}
