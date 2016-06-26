package com.codepath.simpletodo.fragments;

import android.support.v4.content.Loader;

import com.codepath.simpletodo.models.ToDoItem;
import com.yahoo.squidb.data.SquidCursor;

/**
 * Fragment that shows Overdue ToDo items.
 */
public class OverdueToDoListFragment extends BaseTodoListFragment {

    public static OverdueToDoListFragment createInstance() {
        return new OverdueToDoListFragment();
    }

    @Override
    protected Loader<SquidCursor<ToDoItem>> doCreateToDoItemsLoader() {
        return mToDoItemDAO.getOverdueToDoItems();
    }
}
