package com.grapeup.hotelreservation.repository;

import com.grapeup.hotelreservation.model.Reservation;

import java.util.List;

public interface ReservationRepository {

    List<Reservation> findAll();
}
