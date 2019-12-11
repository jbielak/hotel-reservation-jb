package com.grapeup.hotelreservation.repository;

import com.grapeup.hotelreservation.model.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    List<Reservation> findAll();
    Optional<Reservation> findById(Long id);

}
