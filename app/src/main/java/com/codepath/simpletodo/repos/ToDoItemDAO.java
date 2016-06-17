package com.codepath.simpletodo.repos;

import com.codepath.simpletodo.models.ToDoItem;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ToDoItemDAO {
    private final SimpleToDoDatabase mDatabase;

    @Inject
    public ToDoItemDAO(final SimpleToDoDatabase database) {
        mDatabase = database;
    }

    public void insert(final ToDoItem toDoItem) {
        mDatabase.persist(toDoItem);
    }

    public void update(final ToDoItem toDoItem) {
        mDatabase.persist(toDoItem);
    }

    public void delete(final long toDoItemId) {
        mDatabase.delete(ToDoItem.class, toDoItemId);
    }
}