package com.codepath.simpletodo.adapters;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter mAdapter;

    public SimpleItemTouchHelperCallback(final ItemTouchHelperAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public void onSelectedChanged(final RecyclerView.ViewHolder viewHolder,
                                  final int actionState) {
        // We only want the active item
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof ItemTouchHelperViewHolder) {
                ItemTouchHelperViewHolder itemViewHolder =
                        (ItemTouchHelperViewHolder) viewHolder;
                itemViewHolder.onItemSelected();
            }
        }

        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(final RecyclerView recyclerView,
                          final RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if (viewHolder instanceof ItemTouchHelperViewHolder) {
            ItemTouchHelperViewHolder itemViewHolder =
                    (ItemTouchHelperViewHolder) viewHolder;
            itemViewHolder.onItemClear();
        }
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder) {
        int swipeFlags = ItemTouchHelper.END;
        return makeMovementFlags(0, swipeFlags);
    }

    @Override
    public boolean onMove(final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final RecyclerView.ViewHolder target) {
        throw new UnsupportedOperationException("Dragging is NOT supported");
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, final int direction) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition(), viewHolder);
    }
}