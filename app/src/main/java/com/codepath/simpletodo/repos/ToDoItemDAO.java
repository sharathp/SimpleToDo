package com.codepath.simpletodo.repos;

import android.content.Context;

import com.codepath.simpletodo.models.ToDoItem;
import com.yahoo.squidb.sql.Order;
import com.yahoo.squidb.sql.Query;
import com.yahoo.squidb.support.SquidSupportCursorLoader;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ToDoItemDAO {
    private final SimpleToDoDatabase mDatabase;
    private final Context mContext;

    @Inject
    public ToDoItemDAO(final Context context, final SimpleToDoDatabase database) {
        mContext = context;
        mDatabase = database;
    }

    // even though this seems unnecessary, this helps keep track of all clients
    public SquidSupportCursorLoader<ToDoItem> getAllToDoItems() {
        final Query query = Query.select(ToDoItem.PROPERTIES)
                .orderBy(Order.asc(ToDoItem.DUE_DATE))
                // TODO - modify order to be a int
                .orderBy(Order.desc(ToDoItem.PRIORITY))
                .orderBy(Order.asc(ToDoItem.NAME));
        final SquidSupportCursorLoader<ToDoItem> loader = new SquidSupportCursorLoader<>(mContext, mDatabase, ToDoItem.class, query);
        loader.setNotificationUri(ToDoItem.CONTENT_URI);
        return loader;
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