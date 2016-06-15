package com.codepath.simpletodo.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.db.models.TodoItem;

public class ToDoItemView extends LinearLayout {
    private TextView mNotesTextView;
    private TodoItem mItem;

    public ToDoItemView(final Context context) {
        super(context);
        init();
    }

    public ToDoItemView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ToDoItemView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ToDoItemView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        inflate(getContext(), R.layout.row_todo_item,this);
        mNotesTextView  = (TextView)findViewById(R.id.tv_todo_name);
    }

    public void setItem(TodoItem todoItem) {
        mItem = todoItem;
        setupView();
    }

    private void setupView() {
        mNotesTextView.setText(mItem.notes());
    }
}