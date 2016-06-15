package com.codepath.simpletodo.db.models;

import com.codepath.simpletodo.db.adapters.DateAdapter;
import com.google.auto.value.AutoValue;
import com.squareup.sqldelight.ColumnAdapter;
import com.squareup.sqldelight.EnumColumnAdapter;

import java.util.Calendar;

@AutoValue
public abstract class TodoItem implements TodoItemModel {
    private static final DateAdapter DATE_ADAPTER = new DateAdapter();
    private static final ColumnAdapter<Priority> SHOOTS_ADAPTER = EnumColumnAdapter.create(Priority.class);
    private static final ColumnAdapter<Status> POSITION_ADAPTER = EnumColumnAdapter.create(Status.class);

    public static final Mapper<TodoItem> MAPPER = new Mapper<>(new Mapper.Creator<TodoItem>() {
        @Override
        public TodoItem create(final long _id, final String name, final String notes, final Calendar due_date, final Priority priority, final Status status) {
            return new AutoValue_TodoItem(_id, name, notes, due_date, priority, status);
        }
    }, DATE_ADAPTER, SHOOTS_ADAPTER, POSITION_ADAPTER);


    public static TodoItemMarshal marshal() {
        return new TodoItemMarshal(DATE_ADAPTER, SHOOTS_ADAPTER, POSITION_ADAPTER);
    }
}