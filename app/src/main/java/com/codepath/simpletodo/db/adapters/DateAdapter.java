package com.codepath.simpletodo.db.adapters;

import android.content.ContentValues;
import android.database.Cursor;

import com.squareup.sqldelight.ColumnAdapter;

import java.util.Calendar;

public final class DateAdapter implements ColumnAdapter<Calendar> {
    @Override
    public void marshal(final ContentValues contentValues, final String columnName, final Calendar date) {
        contentValues.put(columnName, date.getTimeInMillis());
    }

    @Override
    public Calendar map(final Cursor cursor, final int columnIndex) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(cursor.getLong(columnIndex));
        return calendar;
    }
}