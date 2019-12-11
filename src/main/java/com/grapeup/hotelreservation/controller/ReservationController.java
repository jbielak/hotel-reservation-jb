package com.grapeup.hotelreservation.controller;

import com.grapeup.hotelreservation.model.Reservation;
import com.grapeup.hotelreservation.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private static final String RESERVATIONS_MAPPING = "/reservations/";
    private ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping()
    public List<Reservation> getReservations(@RequestParam(required = false) Long roomId) {
        if (roomId != null) {
            return reservationService.findForRoom(roomId);
        }
        return reservationService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReservation(@PathVariable Long id) {

        return reservationService.findById(id)
                .map(reservation -> {
                    try {
                        return ResponseEntity
                                .ok()
                                .location(new URI(RESERVATIONS_MAPPING + reservation.getId()))
                                .body(reservation);
                    } catch (URISyntaxException e ) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Reservation> createReservation(@RequestBody @Valid Reservation reservation) {
        Reservation newReservation = reservationService.save(reservation);
        try {
            return ResponseEntity
                    .created(new URI(RESERVATIONS_MAPPING + newReservation.getId()))
                    .body(newReservation);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReservation(@RequestBody @Valid Reservation reservation,
                                           @PathVariable Long id) {

        Optional<Reservation> existingReservation = reservationService.findById(id);

        return existingReservation.map(r -> {
            r.setUsername(reservation.getUsername());
            r.setNumberOfPeople(reservation.getNumberOfPeople());
            r.setStartDate(reservation.getStartDate());
            r.setEndDate(reservation.getEndDate());
            r.setRoomId(reservation.getRoomId());

            try {
                if (reservationService.update(r).isPresent()) {
                    return ResponseEntity.ok()
                            .location(new URI(RESERVATIONS_MAPPING + r.getId()))
                            .body(r);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } catch (URISyntaxException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.ok().build();
    }
}
