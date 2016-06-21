package com.codepath.simpletodo.repos;

import android.content.Context;

import com.codepath.simpletodo.models.ToDoItem;
import com.yahoo.squidb.data.SquidDatabase;
import com.yahoo.squidb.data.adapter.SQLiteDatabaseWrapper;
import com.yahoo.squidb.sql.Table;

public class SimpleToDoDatabase extends SquidDatabase {
    private static final String DB_NAME = "simple-todo.db";
    private static final int VERSION = 1;

    public SimpleToDoDatabase(Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return DB_NAME;
    }

    @Override
    protected int getVersion() {
        return VERSION;
    }

    @Override
    protected Table[] getTables() {
        return new Table[]{
            ToDoItem.TABLE
        };
    }

    @Override
    protected boolean onUpgrade(final SQLiteDatabaseWrapper db, final int oldVersion, final int newVersion) {
        // nothing to upgrade yet..
        return false;
    }
}