package com.grapeup.hotelreservation.model;


import com.grapeup.hotelreservation.exception.AvailableRoomNotFoundException;
import com.grapeup.hotelreservation.exception.IncorrectNumberOfPeopleException;

import java.util.Arrays;

public enum RoomType {
    BASIC(4),
    SUITE(6),
    PENTHOUSE(8);

    private int capacity;

    RoomType(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public static RoomType getFittingSize(int size) {
        if (size < 1) {
            throw new IncorrectNumberOfPeopleException("Room size can't be less than 1.");
        }
        return Arrays.stream(RoomType.values())
                .filter(roomType -> roomType.capacity >= size)
                .findFirst()
                .orElseThrow(() -> new IncorrectNumberOfPeopleException(
                        String.format("Room size %d don't fit. Maximum "
                                + "possible size is %d.", size, PENTHOUSE.capacity)));
    }
}
