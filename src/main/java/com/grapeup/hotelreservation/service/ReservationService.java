package com.grapeup.hotelreservation.service;

import com.grapeup.hotelreservation.model.Reservation;
import java.util.List;
import java.util.Optional;

public interface ReservationService {

    List<Reservation> findAll();
    Optional<Reservation> findById(Long id);
    Reservation save(Reservation reservation);
}
