package com.codepath.simpletodo.fragments;

import android.support.v4.content.Loader;

import com.codepath.simpletodo.models.ToDoItem;
import com.yahoo.squidb.data.SquidCursor;

/**
 * Fragment that displays all the ToDo items.
 */
public class AllToDoListFragment extends BaseTodoListFragment {

    public static AllToDoListFragment createInstance() {
        return new AllToDoListFragment();
    }

    @Override
    protected Loader<SquidCursor<ToDoItem>> doCreateToDoItemsLoader() {
        return mToDoItemDAO.getAllToDoItems();
    }
}