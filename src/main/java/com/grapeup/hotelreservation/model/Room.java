package com.grapeup.hotelreservation.model;

import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Room {

    private Long id;
    private RoomType roomType;
    private Set<Reservation> reservations;

}