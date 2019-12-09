package com.grapeup.hotelreservation.controller;

import com.grapeup.hotelreservation.model.Reservation;
import com.grapeup.hotelreservation.service.ReservationService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping()
    public List<Reservation> getReservations() {
        return reservationService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReservation(@PathVariable Long id) {

        return reservationService.findById(id)
                .map(reservation -> {
                    try {
                        return ResponseEntity
                                .ok()
                                .location(new URI("/reservations/" + reservation.getId()))
                                .body(reservation);
                    } catch (URISyntaxException e ) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
