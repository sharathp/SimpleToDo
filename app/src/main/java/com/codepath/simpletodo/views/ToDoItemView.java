package com.codepath.simpletodo.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.models.ToDoItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ToDoItemView extends LinearLayout {

    private ToDoItem mItem;

    @BindView(R.id.text_name)
    TextView mNotesTextView;

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
        inflate(getContext(), R.layout.merge_item_todo_item,this);
        ButterKnife.bind(this);
    }

    public void setItem(final ToDoItem todoItem) {
        mItem = todoItem;
        setupView();
    }

    private void setupView() {
        mNotesTextView.setText(mItem.getName());
    }
}
