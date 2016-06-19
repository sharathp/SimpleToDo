package com.codepath.simpletodo.models;

public enum Priority {
    HIGH(2),
    MEDIUM(1),
    LOW(0);

    private final int mOrder;

    Priority(final int order) {
        mOrder = order;
    }

    public int getOrder() {
        return mOrder;
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