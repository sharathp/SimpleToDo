package com.codepath.simpletodo.fragments;

import android.os.Bundle;
import android.support.v4.content.Loader;

import com.codepath.simpletodo.models.ToDoItem;
import com.yahoo.squidb.data.SquidCursor;

/**
 * Default fragment that is displayed in application. This shows current
 * ToDo items (i.e., only today)
 */
public class HomeToDoListFragment extends BaseTodoListFragment {

    public static HomeToDoListFragment createInstance() {
        return new HomeToDoListFragment();
    }

    @Override
    protected boolean isItemCreationSupported() {
        return true;
    }

    @Override
    protected Loader<SquidCursor<ToDoItem>> doCreateToDoItemsLoader(final int id, final Bundle args) {
        return mToDoItemDAO.getCurrentToDoItems();
    }
}