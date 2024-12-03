package com.example.everyday;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.everyday.ui.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    private final SQLiteDatabase database;

    public TaskRepository(Context context) {
        TaskDatabaseHelper dbHelper = new TaskDatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public void addTask(Task task) {
        ContentValues values = new ContentValues();
        values.put(TaskDatabaseHelper.COLUMN_ID, task.getId());
        values.put(TaskDatabaseHelper.COLUMN_DESCRIPTION, task.getDescription());
        values.put(TaskDatabaseHelper.COLUMN_YEAR, task.getYear());
        values.put(TaskDatabaseHelper.COLUMN_MONTH, task.getMonth());
        values.put(TaskDatabaseHelper.COLUMN_DAY, task.getDay());
        values.put(TaskDatabaseHelper.COLUMN_HOUR, task.getHour());
        values.put(TaskDatabaseHelper.COLUMN_MINUTE, task.getMinute());
        values.put(TaskDatabaseHelper.COLUMN_DONE, task.isDone() ? 1 : 0);

        database.insert(TaskDatabaseHelper.TABLE_TASKS, null, values);
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        Cursor cursor = database.query(TaskDatabaseHelper.TABLE_TASKS, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            Task task = new Task(
                    cursor.getString(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_DESCRIPTION)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_YEAR)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_MONTH)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_DAY)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_HOUR)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_MINUTE))
            );
            task.setId(cursor.getString(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_ID)));
            task.setDone(cursor.getInt(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_DONE)) == 1);
            tasks.add(task);
        }
        cursor.close();
        return tasks;
    }

    public void updateTask(Task task) {
        ContentValues values = new ContentValues();
        values.put(TaskDatabaseHelper.COLUMN_DESCRIPTION, task.getDescription());
        values.put(TaskDatabaseHelper.COLUMN_YEAR, task.getYear());
        values.put(TaskDatabaseHelper.COLUMN_MONTH, task.getMonth());
        values.put(TaskDatabaseHelper.COLUMN_DAY, task.getDay());
        values.put(TaskDatabaseHelper.COLUMN_HOUR, task.getHour());
        values.put(TaskDatabaseHelper.COLUMN_MINUTE, task.getMinute());
        values.put(TaskDatabaseHelper.COLUMN_DONE, task.isDone() ? 1 : 0);

        database.update(TaskDatabaseHelper.TABLE_TASKS, values, TaskDatabaseHelper.COLUMN_ID + " = ?", new String[]{task.getId()});
    }

    public void deleteTask(String taskId) {
        database.delete(TaskDatabaseHelper.TABLE_TASKS, TaskDatabaseHelper.COLUMN_ID + " = ?", new String[]{taskId});
    }

    public Task getTaskById(String taskId) {
        // Define the selection clause
        String selection = TaskDatabaseHelper.COLUMN_ID + " = ?";
        // Define the selection arguments
        String[] selectionArgs = {taskId};

        // Perform the query
        Cursor cursor = database.query(
                TaskDatabaseHelper.TABLE_TASKS,   // Table name
                null,                             // Columns to retrieve (all columns)
                selection,                        // Selection clause
                selectionArgs,                    // Selection arguments
                null,                             // Group by
                null,                             // Having
                null                              // Order by
        );

        Task task = null;
        if (cursor.moveToFirst()) {  // Check if there is at least one result
            // Retrieve data from the Cursor
            String description = cursor.getString(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_DESCRIPTION));
            int year = cursor.getInt(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_YEAR));
            int month = cursor.getInt(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_MONTH));
            int day = cursor.getInt(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_DAY));
            int hour = cursor.getInt(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_HOUR));
            int minute = cursor.getInt(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_MINUTE));
            String id = cursor.getString(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_ID));
            boolean done = cursor.getInt(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_DONE)) == 1;

            // Create a Task object
            task = new Task(description, year, month, day, hour, minute);
            task.setId(id);
            task.setDone(done);
        }
        cursor.close();  // Close the Cursor

        return task;
    }
}
