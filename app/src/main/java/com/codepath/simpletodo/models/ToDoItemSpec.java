package com.codepath.simpletodo.models;

import android.net.Uri;

import com.yahoo.squidb.annotations.ColumnSpec;
import com.yahoo.squidb.annotations.TableModelSpec;

@TableModelSpec(className="ToDoItem", tableName="todo_item")
public class ToDoItemSpec {

    public static final Uri CONTENT_URI = Uri.parse("content://com.codepath.simpletodo/todo-items");

    @ColumnSpec(constraints = "NOT NULL")
    public String name;

    public String notes;

    @ColumnSpec(constraints = "NOT NULL")
    public long dueDate;

    @ColumnSpec(constraints = "NOT NULL")
    private Priority priority;

    @ColumnSpec(constraints = "NOT NULL")
    private Status status;
}