package com.codepath.simpletodo.repos;

import android.content.Context;

import com.codepath.simpletodo.models.Priority;
import com.codepath.simpletodo.models.ToDoItem;
import com.codepath.simpletodo.utils.DateUtils;
import com.yahoo.squidb.data.SquidCursor;
import com.yahoo.squidb.sql.Delete;
import com.yahoo.squidb.sql.Order;
import com.yahoo.squidb.sql.Query;
import com.yahoo.squidb.support.SquidSupportCursorLoader;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.yahoo.squidb.sql.CaseInsensitiveLikeCriterion.insensitiveLike;

@Singleton
// even though the methods returning SquidSupportCursorLoader seems unnecessary,
// this helps keep track of all clients
public class ToDoItemDAO {
    private final SimpleToDoDatabase mDatabase;
    private final Context mContext;

    @Inject
    public ToDoItemDAO(final Context context, final SimpleToDoDatabase database) {
        mContext = context;
        mDatabase = database;
    }

    /**
     * Return all the current and future ToDoItems.
     *
     * @return current and future ToDoItems
     */
    public SquidSupportCursorLoader<ToDoItem> getCurrentToDoItems() {
        return getToDoItems(getTodayQuery());
    }

    /**
     * Return all ToDo items matching the give text string in name or description.
     *
     * @return current and future ToDoItems
     */
    public SquidSupportCursorLoader<ToDoItem> getMatchingToDoItems(final String searchString) {
        final Query query = Query.select(ToDoItem.PROPERTIES)
                .where(insensitiveLike(ToDoItem.NAME, searchString).or(insensitiveLike(ToDoItem.DESCRIPTION, searchString)))
                .orderBy(Order.asc(ToDoItem.DUE_DATE))
                .orderBy(Order.desc(ToDoItem.PRIORITY))
                .orderBy(Order.asc(ToDoItem.NAME));
        return getToDoItems(query);
    }

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

    /**
     * Return all overdue ToDo Items.
     *
     * @return all overdue ToDo Items
     */
    public SquidSupportCursorLoader<ToDoItem> getOverdueToDoItems() {
        final Query query = Query.select(ToDoItem.PROPERTIES)
                .where(ToDoItem.DUE_DATE.lt(DateUtils.getToday().getTime()).and(ToDoItem.COMPLETED.eq(false)))
                .orderBy(Order.asc(ToDoItem.DUE_DATE))
                .orderBy(Order.desc(ToDoItem.PRIORITY))
                .orderBy(Order.asc(ToDoItem.NAME));
        return getToDoItems(query);
    }

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

    public void persist(final ToDoItem toDoItem) {
        mDatabase.persist(toDoItem);
    }

    public void delete(final long toDoItemId) {
        mDatabase.delete(ToDoItem.class, toDoItemId);
    }

    public void deleteAll() {
        mDatabase.deleteAll(ToDoItem.class);
    }

    public void deleteCompleted() {
        mDatabase.delete(Delete.from(ToDoItem.TABLE).where(ToDoItem.COMPLETED.eq(true)));
    }

    private Query getTodayQuery() {
        return Query.select(ToDoItem.PROPERTIES)
                .where(ToDoItem.DUE_DATE.eq(DateUtils.getToday().getTime()))
                .orderBy(Order.desc(ToDoItem.PRIORITY))
                .orderBy(Order.asc(ToDoItem.NAME));
    }

    private SquidSupportCursorLoader<ToDoItem> getToDoItems(final Query query) {
        final SquidSupportCursorLoader<ToDoItem> loader = new SquidSupportCursorLoader<>(mContext, mDatabase, ToDoItem.class, query);
        loader.setNotificationUri(ToDoItem.CONTENT_URI);
        return loader;
    }
}