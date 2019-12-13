package com.grapeup.hotelreservation.service;

import com.grapeup.hotelreservation.model.Reservation;
import com.grapeup.hotelreservation.model.Room;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefaultRoomService implements RoomService {

    @Override
    public Optional<Room> assignRoom(Reservation reservation) {
        return null;
    }

    @Override
    public Optional<Room> reassignRoom(Reservation reservation) {
        return null;
    }
}
