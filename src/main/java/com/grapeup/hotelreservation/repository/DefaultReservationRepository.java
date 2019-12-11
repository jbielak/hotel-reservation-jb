package com.grapeup.hotelreservation.repository;

import com.grapeup.hotelreservation.model.Reservation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DefaultReservationRepository implements ReservationRepository {

    @Override
    public List<Reservation> findAll() {
        return null;
    }
}
