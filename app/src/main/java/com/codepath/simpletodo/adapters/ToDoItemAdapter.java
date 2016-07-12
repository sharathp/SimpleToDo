package com.codepath.simpletodo.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.models.ToDoItem;
import com.codepath.simpletodo.services.ToDoItemPersistenceService;
import com.codepath.simpletodo.views.ToDoItemView;
import com.yahoo.squidb.recyclerview.SquidRecyclerAdapter;
import com.yahoo.squidb.recyclerview.SquidViewHolder;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;

public class ToDoItemAdapter extends SquidRecyclerAdapter<ToDoItem, ToDoItemAdapter.ToDoItemHolder>
        implements ItemTouchHelperAdapter {
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

    @Override
    public void onItemDismiss(final int position, final RecyclerView.ViewHolder viewHolder) {
        Log.i("ToDoItemAdapter", "dismiss: " + position);
        deleteItem(viewHolder.itemView.getContext(), ((ToDoItemHolder)viewHolder).item);
    }

    private void deleteItem(final Context context, final ToDoItem toDoItem) {
        final Intent deleteIntent = ToDoItemPersistenceService.createIntentToDelete(context, toDoItem.getId());
        context.startService(deleteIntent);
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