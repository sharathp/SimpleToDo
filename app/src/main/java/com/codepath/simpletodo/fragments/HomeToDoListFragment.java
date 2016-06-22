package com.codepath.simpletodo.fragments;

import android.support.v4.content.Loader;

import com.codepath.simpletodo.models.ToDoItem;
import com.yahoo.squidb.data.SquidCursor;

/**
 * Default fragment that is displayed in application. This shows current
 * ToDo items (i.e., only today and future items)
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
    protected Loader<SquidCursor<ToDoItem>> doCreateToDoItemsLoader() {
        return mToDoItemDAO.getCurrentToDoItems();
    }
}
