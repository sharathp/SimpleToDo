package com.codepath.simpletodo.repos;

import android.content.Context;

import com.codepath.simpletodo.models.Priority;
import com.codepath.simpletodo.models.ToDoItem;
import com.codepath.simpletodo.utils.DateUtils;
import com.yahoo.squidb.data.SquidCursor;
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

    /**
     * Return all the current and future ToDoItems.
     *
     * @return current and future ToDoItems
     */
    public SquidSupportCursorLoader<ToDoItem> getCurrentToDoItems() {
        return getToDoItems(getTodayQuery());
    }

    // even though this seems unnecessary, this helps keep track of all clients

    /**
     * Return all High Priority ToDo Items.
     *
     * @return all High Prioiry ToDo Items
     */
    public SquidSupportCursorLoader<ToDoItem> getHighPriorityToDoItems() {
        final Query query = Query.select(ToDoItem.PROPERTIES)
                .where(ToDoItem.PRIORITY.eq(Priority.HIGH.getOrder()))
                .orderBy(Order.asc(ToDoItem.DUE_DATE))
                .orderBy(Order.asc(ToDoItem.NAME));
        return getToDoItems(query);
    }

    // even though this seems unnecessary, this helps keep track of all clients

    /**
     * Return all ToDoItems.
     *
     * @return all ToDoItems
     */
    public SquidSupportCursorLoader<ToDoItem> getAllToDoItems() {
        final Query query = Query.select(ToDoItem.PROPERTIES)
                .orderBy(Order.asc(ToDoItem.DUE_DATE))
                .orderBy(Order.desc(ToDoItem.PRIORITY))
                .orderBy(Order.asc(ToDoItem.NAME));
        return getToDoItems(query);
    }

    public SquidCursor<ToDoItem> getAllToDoItemsForToday() {
        return mDatabase.query(ToDoItem.class, getTodayQuery());
    }

    private Query getTodayQuery() {
        return Query.select(ToDoItem.PROPERTIES)
                .where(ToDoItem.DUE_DATE.eq(DateUtils.getToday().getTime()))
                .orderBy(Order.desc(ToDoItem.PRIORITY))
                .orderBy(Order.asc(ToDoItem.NAME));
    }

    // even though this seems unnecessary, this helps keep track of all clients
    private SquidSupportCursorLoader<ToDoItem> getToDoItems(final Query query) {
        final SquidSupportCursorLoader<ToDoItem> loader = new SquidSupportCursorLoader<>(mContext, mDatabase, ToDoItem.class, query);
        loader.setNotificationUri(ToDoItem.CONTENT_URI);
        return loader;
    }

    public void persist(final ToDoItem toDoItem) {
        mDatabase.persist(toDoItem);
    }

    public void delete(final long toDoItemId) {
        mDatabase.delete(ToDoItem.class, toDoItemId);
    }

    public void deleteAll() {
        mDatabase.deleteAll(ToDoItem.class);
    }
}