package com.codepath.simpletodo.fragments;

import android.os.Bundle;
import android.support.v4.content.Loader;

import com.codepath.simpletodo.models.ToDoItem;
import com.yahoo.squidb.data.SquidCursor;

/**
 * Fragment that shows High Priority ToDo items.
 */
public class HighPriorityToDoListFragment extends BaseTodoListFragment {

    public static HighPriorityToDoListFragment createInstance() {
        return new HighPriorityToDoListFragment();
    }

    @Override
    protected Loader<SquidCursor<ToDoItem>> doCreateToDoItemsLoader(final int id, final Bundle args) {
        return mToDoItemDAO.getHighPriorityToDoItems();
    }
}