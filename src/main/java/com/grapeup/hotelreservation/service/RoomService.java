package com.grapeup.hotelreservation.service;

import com.grapeup.hotelreservation.model.Reservation;
import java.util.Optional;

public interface RoomService {

    Optional<Long> assignRoom(Reservation reservation);
}
