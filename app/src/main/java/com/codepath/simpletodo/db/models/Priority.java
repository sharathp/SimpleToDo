package com.codepath.simpletodo.db.models;

public enum Priority {
    HIGH(0),
    MEDIUM(1),
    LOW(2);

    private final int mOrder;

    Priority(int order) {
        mOrder = order;
    }

    public int getOrder() {
        return mOrder;
    }
}