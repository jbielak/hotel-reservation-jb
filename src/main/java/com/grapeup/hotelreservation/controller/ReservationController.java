package com.grapeup.hotelreservation.controller;

import com.grapeup.hotelreservation.model.Reservation;
import com.grapeup.hotelreservation.service.ReservationService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/reservation/{id}")
    public ResponseEntity<?> getReservation(@PathVariable Long id) {

        return reservationService.findById(id)
                .map(reservation -> {
                    try {
                        return ResponseEntity
                                .ok()
                                .location(new URI("/reservation/" + reservation.getId()))
                                .body(reservation);
                    } catch (URISyntaxException e ) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }
}