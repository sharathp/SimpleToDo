package com.codepath.simpletodo.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.models.ToDoItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ToDoItemView extends RelativeLayout {

    private ToDoItem mItem;

    @BindView(R.id.tv_name)
    TextView mNameTextView;

    @BindView(R.id.tv_due_date)
    TextView mDueDateTextView;

    @BindView(R.id.view_priority)
    View mPriorityIndicatorView;

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
        bindItem();
    }

    private void bindItem() {
        mNameTextView.setText(mItem.getName());
        if (mItem.isCompleted()) {
            mNameTextView.setPaintFlags(mNameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            mNameTextView.setPaintFlags(mNameTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        final String relativeTime = DateUtils.getRelativeTimeSpanString(mItem.getDueDate(), System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_RELATIVE).toString();
        mDueDateTextView.setText(relativeTime);
        mPriorityIndicatorView.setBackgroundColor(getResources().getColor(mItem.getPriority().getColorResourceId()));
    }
}
