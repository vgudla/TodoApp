package com.example.vgudla.todoapp.persistence;

import android.support.annotation.NonNull;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to persist tasks to filesystem
 */
public class FileHelper {
    public static final String TASKS_FILE = "todo.txt";

    private List<TodoTask> getAllTasks(File filesDir) {
        List<TodoTask> items = new ArrayList<>();
        File todoFile = getFileHandle(filesDir);
        try {
            List<String> textLines = new ArrayList<>(FileUtils.readLines(todoFile));
            for (String textLine : textLines) {
                TodoTask task = new TodoTask(textLine);
                items.add(task);
            }
        } catch (Exception ex) {
            items = new ArrayList<>();
        }
        return items;
    }

    @NonNull
    private File getFileHandle(File filesDir) {
        return new File(filesDir, TASKS_FILE);
    }

    private void writeItems(List<TodoTask> todoTasks, File filesDir) {
        File todoFile = getFileHandle(filesDir);
        try {
            FileUtils.writeLines(todoFile, todoTasks);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
