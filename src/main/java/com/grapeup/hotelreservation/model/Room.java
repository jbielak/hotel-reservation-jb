package com.grapeup.hotelreservation.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class Room {

    private Long id;
    private RoomType roomType;
    private Set<Reservation> reservations;

}