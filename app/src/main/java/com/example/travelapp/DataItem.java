package com.example.travelapp;

public class DataItem {
    public static final int TYPE_FLIGHT = 0;
    public static final int TYPE_HOTEL = 1;

    private int type;
    private Object data;

    // Constructor
    public DataItem(int type, Object data) {
        this.type = type;
        this.data = data;
    }

    // Getters
    public int getType() {
        return type;
    }

    public Object getData() {
        return data;
    }
}

