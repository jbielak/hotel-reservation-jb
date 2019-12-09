package com.grapeup.hotelreservation.controller;

import com.grapeup.hotelreservation.model.Reservation;
import com.grapeup.hotelreservation.service.ReservationService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservationController {

    private ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public List<Reservation> getReservations() {
        return reservationService.findAll();
    }

}
