package com.codepath.simpletodo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SimpleToDoOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "simple-todo.db";
    private static final int VERSION = 1;

    public SimpleToDoOpenHelper(final Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        // no-op
    }
}
