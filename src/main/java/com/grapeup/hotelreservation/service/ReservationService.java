package com.grapeup.hotelreservation.service;

import com.grapeup.hotelreservation.model.Reservation;
import java.util.List;

public interface ReservationService {

    List<Reservation> findAll();
}
