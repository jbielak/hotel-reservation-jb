package com.grapeup.hotelreservation.service;

import com.grapeup.hotelreservation.model.Reservation;
import com.grapeup.hotelreservation.model.Room;

import java.util.Optional;

public interface RoomService {

    boolean areDatesAvailableInCurrentRoom(Reservation reservation);
    Optional<Room> assignRoom(Reservation reservation);

}
