package com.codepath.simpletodo.models;

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