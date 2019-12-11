package com.grapeup.hotelreservation.service;

import com.grapeup.hotelreservation.model.Reservation;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class DefaultRoomService implements RoomService {

    @Override
    public Optional<Long> assignRoom(Reservation reservation) {
        return null;
    }
}
