package com.codepath.simpletodo.adapters;

import android.support.v7.widget.RecyclerView;

public interface ItemTouchHelperAdapter {

    /**
     * Called when an item has been dismissed by a swipe.<br/>
     * <br/>
     * Implementations should call {@link RecyclerView.Adapter#notifyItemRemoved(int)} after
     * adjusting the underlying data to reflect this removal.
     *
     * @param position The position of the item dismissed.
     * @param holder The view holder dismissed
     *
     * @see RecyclerView#getAdapterPositionFor(RecyclerView.ViewHolder)
     * @see RecyclerView.ViewHolder#getAdapterPosition()
     */
    void onItemDismiss(int position, RecyclerView.ViewHolder holder);
}
