package com.codepath.simpletodo.models;

import com.codepath.simpletodo.R;

public enum Priority {
    HIGH(2, R.color.priority_high),
    MEDIUM(1, R.color.priority_medium),
    LOW(0, R.color.priority_low);

    private final int mOrder;
    private final int mColorResourceId;

    Priority(final int order, final int colorResourceId) {
        mOrder = order;
        mColorResourceId = colorResourceId;
    }

    public int getOrder() {
        return mOrder;
    }

    public int getColorResourceId() {
        return mColorResourceId;
    }

    public static Priority getPriorityByOrder(final int order) {
        for (Priority current: Priority.values()) {
            if (order == current.getOrder()) {
                return current;
            }
        }
        return null;
    }
}