package com.grapeup.hotelreservation.model;

import java.util.Arrays;

public enum RoomType {
    BASIC(4),
    SUITE(6),
    PENTHOUSE(8);

    private int capacity;

    RoomType(int capacity) {
        this.capacity =  capacity;
    }

    public static RoomType getByValue(int value) {
        return Arrays.stream(RoomType.values())
                .filter(roomType -> roomType.capacity == value)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("Unsupported type %s.", value)));
    }

    public static RoomType getNextSize(int size) {
        return Arrays.stream(RoomType.values())
                .filter(roomType -> roomType.capacity >= size)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Room size %d don't fit.", size)));
                //.orElse(RoomType.PENTHOUSE);
    }

    public int getCapacity() {
        return capacity;
    }

}
