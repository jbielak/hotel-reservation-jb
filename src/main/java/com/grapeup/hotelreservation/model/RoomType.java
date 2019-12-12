package com.grapeup.hotelreservation.model;

public enum RoomType {
    BASIC(4),
    SUITE(6),
    PENTHOUSE(8);

    private int capacity;

    RoomType(int capacity) {
        this.capacity =  capacity;
    }

}
