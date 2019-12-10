package com.grapeup.hotelreservation.controller;

import com.grapeup.hotelreservation.model.Reservation;
import com.grapeup.hotelreservation.service.ReservationService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/reservations")
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        Reservation newReservation = reservationService.save(reservation);
        try {
            return ResponseEntity
                    .created(new URI("/reservations/" + newReservation.getId()))
                    .body(newReservation);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/reservations/{id}")
    public ResponseEntity<?> updateReservation(@RequestBody Reservation reservation,
                                           @PathVariable Long id) {

        Optional<Reservation> existingReservation = reservationService.findById(id);

        return existingReservation.map(r -> {
            r.setUsername(reservation.getUsername());
            r.setNumberOfPeople(reservation.getNumberOfPeople());
            r.setStartDate(reservation.getStartDate());
            r.setEndDate(reservation.getEndDate());
            r.setRoomId(reservation.getRoomId());

            try {
                if (reservationService.update(r)) {
                    return ResponseEntity.ok()
                            .location(new URI("/reservations/" + r.getId()))
                            .body(r);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } catch (URISyntaxException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long id) {

        Optional<Reservation> existingReservation = reservationService.findById(id);

        return existingReservation.map(p -> {
            if (reservationService.delete(p.getId())) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }).orElse(ResponseEntity.notFound().build());
    }
}
