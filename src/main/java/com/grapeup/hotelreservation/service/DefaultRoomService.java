package com.grapeup.hotelreservation.service;

import com.grapeup.hotelreservation.model.Reservation;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefaultRoomService implements RoomService {

    @Override
    public Optional<Long> assignRoom(Reservation reservation) {
        return null;
    }

    @Override
    public Optional<Long> reassignRoom(Reservation reservation) {
        return null;
    }
}
