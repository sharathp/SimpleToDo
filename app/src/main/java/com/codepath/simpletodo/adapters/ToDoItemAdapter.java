package com.codepath.simpletodo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.models.ToDoItem;
import com.codepath.simpletodo.views.ToDoItemView;
import com.yahoo.squidb.recyclerview.SquidRecyclerAdapter;
import com.yahoo.squidb.recyclerview.SquidViewHolder;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;

public class ToDoItemAdapter extends SquidRecyclerAdapter<ToDoItem, ToDoItemAdapter.ToDoItemHolder> {
    private final WeakReference<ToDoItemClickListener> mToDoItemClickListener;

    public ToDoItemAdapter(final ToDoItemClickListener toDoItemClickListener) {
        mToDoItemClickListener = new WeakReference<>(toDoItemClickListener);
    }

    @Override
    public void onBindSquidViewHolder(final ToDoItemHolder holder, final int position) {
        holder.bindToDoItem();
    }

    @Override
    public ToDoItemHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.item_todo_item, parent, false);
        return new ToDoItemHolder(view, mToDoItemClickListener);
    }

    public static class ToDoItemHolder extends SquidViewHolder<ToDoItem> {
        private final WeakReference<ToDoItemClickListener> mToDoItemClickListener;

        // this prevents creating listener instances every time bind is invoked
        private View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final ToDoItemClickListener toDoItemClickListener = mToDoItemClickListener.get();
                if (toDoItemClickListener != null) {
                    toDoItemClickListener.onClick(item);
                }
            }
        };

        public ToDoItemHolder(final View itemView, final WeakReference<ToDoItemClickListener> toDoItemClickListener) {
            // empty model as recommended in SquidViewHolder documentation..
            super(itemView, new ToDoItem());
            mToDoItemClickListener = toDoItemClickListener;
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(mOnClickListener);
        }

        public void bindToDoItem() {
            ((ToDoItemView) itemView).setItem(item);
        }
    }

    public interface ToDoItemClickListener {
        void onClick(ToDoItem toDoItem);
    }
}