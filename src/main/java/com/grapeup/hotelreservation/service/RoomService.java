package com.grapeup.hotelreservation.service;

import com.grapeup.hotelreservation.model.Reservation;
import com.grapeup.hotelreservation.model.Room;

import java.util.Optional;

public interface RoomService {

    Optional<Room> assignRoom(Reservation reservation);
    Optional<Room> reassignRoom(Reservation reservation);
}
