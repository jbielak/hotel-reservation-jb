package com.grapeup.hotelreservation.controller;

import com.grapeup.hotelreservation.converter.ReservationConverter;
import com.grapeup.hotelreservation.dto.ReservationDto;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private static final String RESERVATIONS_MAPPING = "/reservations/";
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping()
    public List<ReservationDto> getReservations(@RequestParam(required = false) Long roomId) {
        if (roomId != null) {
            return reservationService.findForRoom(roomId).stream()
                    .map(ReservationConverter::toDto)
                    .collect(Collectors.toList());
        }
        return reservationService.findAll().stream()
                .map(ReservationConverter::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReservation(@PathVariable Long id) {

        return reservationService.findById(id)
                .map(reservation -> {
                    try {
                        return ResponseEntity
                                .ok()
                                .location(new URI(RESERVATIONS_MAPPING + reservation.getId()))
                                .body(ReservationConverter.toDto(reservation));
                    } catch (URISyntaxException e ) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<ReservationDto> createReservation(@RequestBody @Valid ReservationDto reservation) {
        Reservation newReservation = reservationService.save(ReservationConverter.toEntity(reservation));
        try {
            return ResponseEntity
                    .created(new URI(RESERVATIONS_MAPPING + newReservation.getId()))
                    .body(ReservationConverter.toDto(newReservation));
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReservation(@RequestBody @Valid ReservationDto reservation,
                                           @PathVariable Long id) {

        Optional<Reservation> existingReservation = reservationService.findById(id);

        return existingReservation.map(r -> {
            r.setUsername(reservation.getUsername());
            r.setNumberOfPeople(reservation.getNumberOfPeople());
            r.setStartDate(reservation.getStartDate());
            r.setEndDate(reservation.getEndDate());

            try {
                if (reservationService.update(r).isPresent()) {
                    return ResponseEntity.ok()
                            .location(new URI(RESERVATIONS_MAPPING + r.getId()))
                            .body(ReservationConverter.toDto(r));
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
