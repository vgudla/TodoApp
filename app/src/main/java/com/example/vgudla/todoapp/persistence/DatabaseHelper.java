package com.example.vgudla.todoapp.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple helper class for DB CRUD operations
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "todoTasksDb";
    private static final int DATABASE_VERSION = 2;
    private static final String TAG = "DatabaseHelper";

    private static final String TASKS_TABLE = "tasks";
    private static final String TASK_NAME = "task";
    private static final String TASK_DATE = "taskDate";
    private static final String TASK_PRIORITY = "taskPriority";

    private static DatabaseHelper databaseHelper;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context.getApplicationContext());
        }
        return databaseHelper;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TASKS_TABLE +
                "(" +
                    TASK_NAME + " TEXT PRIMARY KEY," +
                    TASK_DATE + " TEXT," +
                    TASK_PRIORITY + " TEXT" +
                ")";

        db.execSQL(CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TASKS_TABLE);
            onCreate(db);
        }
    }

    public void addTask(TodoTask task) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(TASK_NAME, task.getTaskText());
            values.put(TASK_DATE, task.getTaskCompletionDate());
            values.put(TASK_PRIORITY, task.getTaskPriority());
            db.insertOrThrow(TASKS_TABLE, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add task to database");
        } finally {
            db.endTransaction();
        }
    }

    public void updateTask(String key, TodoTask task) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(TASK_NAME, task.getTaskText());
            values.put(TASK_DATE, task.getTaskCompletionDate());
            values.put(TASK_PRIORITY, task.getTaskPriority());
            int rows = db.update(TASKS_TABLE, values, TASK_NAME + "= ?", new String[]{key});

            if (rows == 1) {
                // Get the primary key of the user we just updated
                String taskSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        TASK_NAME, TASKS_TABLE, TASK_NAME);
                Cursor cursor = db.rawQuery(taskSelectQuery, new String[]{String.valueOf(task.getTaskText())});
                try {
                    if (cursor.moveToFirst()) {
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                db.insertOrThrow(TASKS_TABLE, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add or update task");
        } finally {
            db.endTransaction();
        }
    }

    public void deleteTask(TodoTask task) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TASKS_TABLE, TASK_NAME + "= ?", new String[]{task.getTaskText()});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all posts and users");
        } finally {
            db.endTransaction();
        }
    }

    public List<TodoTask> getAllTasks() {
        List<TodoTask> tasks = new ArrayList<>();

        String TASKS_SELECT_QUERY =
                String.format("SELECT * FROM %s ",
                        TASKS_TABLE);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(TASKS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    String taskText = cursor.getString(cursor.getColumnIndex(TASK_NAME));
                    String taskCompletionDate = cursor.getString(cursor.getColumnIndex(TASK_DATE));
                    String taskPriority = cursor.getString(cursor.getColumnIndex(TASK_PRIORITY));
                    TodoTask task = new TodoTask(taskText, taskPriority, taskCompletionDate);
                    tasks.add(task);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get tasks from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return tasks;
    }
}
