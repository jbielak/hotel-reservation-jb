package com.grapeup.hotelreservation.model;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Room {
    private Long id;
    private RoomType roomType;
    private Set<Reservation> reservations = new HashSet<>();
}
