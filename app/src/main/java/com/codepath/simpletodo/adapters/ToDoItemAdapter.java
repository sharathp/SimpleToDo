package com.codepath.simpletodo.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.codepath.simpletodo.models.ToDoItem;
import com.codepath.simpletodo.views.ToDoItemView;
import com.yahoo.squidb.recyclerview.SquidRecyclerAdapter;
import com.yahoo.squidb.recyclerview.SquidViewHolder;

import butterknife.ButterKnife;

public class ToDoItemAdapter extends SquidRecyclerAdapter<ToDoItem, ToDoItemAdapter.ToDoItemHolder> {

    @Override
    public void onBindSquidViewHolder(final ToDoItemHolder holder, final int position) {
        holder.bindToDoItem();
    }

    @Override
    public ToDoItemHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final ToDoItemView view = new ToDoItemView(parent.getContext());
        return new ToDoItemHolder(view);
    }

    public static class ToDoItemHolder extends SquidViewHolder<ToDoItem> {
        // this prevents creating a listener instance every time bind is invoked
        private View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // no-op
            }
        };

        public ToDoItemHolder(final View itemView) {
            // empty model as recommended in SquidViewHolder documentation..
            super(itemView, new ToDoItem());
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(mOnClickListener);
        }

        public void bindToDoItem() {
            ((ToDoItemView) itemView).setItem(item);
        }
    }
}