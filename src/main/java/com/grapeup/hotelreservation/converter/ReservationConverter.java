package com.grapeup.hotelreservation.converter;

import com.grapeup.hotelreservation.dto.ReservationDto;
import com.grapeup.hotelreservation.model.Reservation;
import com.grapeup.hotelreservation.model.Room;

public abstract class ReservationConverter {

    public static ReservationDto toDto(Reservation reservation) {
        return ReservationDto.builder()
                .id(reservation.getId())
                .username(reservation.getUsername())
                .numberOfPeople(reservation.getNumberOfPeople())
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .roomId(reservation.getRoom().getId())
                .build();
    }

    public static Reservation toEntity(ReservationDto reservationDto) {
        return Reservation.builder()
                .id(reservationDto.getId())
                .username(reservationDto.getUsername())
                .numberOfPeople(reservationDto.getNumberOfPeople())
                .startDate(reservationDto.getStartDate())
                .endDate(reservationDto.getEndDate())
                .room(Room.builder().id(reservationDto.getRoomId()).build())
                .build();
    }

}
