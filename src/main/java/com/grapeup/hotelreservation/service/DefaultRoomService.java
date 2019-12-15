package com.grapeup.hotelreservation.service;

import com.grapeup.hotelreservation.model.Reservation;
import com.grapeup.hotelreservation.model.Room;
import com.grapeup.hotelreservation.model.RoomType;
import com.grapeup.hotelreservation.repository.RoomRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefaultRoomService implements RoomService {

    private RoomRepository roomRepository;

    public DefaultRoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public boolean areDatesAvailableInCurrentRoom(Reservation reservation) {
        List<Reservation> reservationsWithOverlappingDates =
                reservation.getRoom().getReservations().stream()
                .filter(r -> areIntervalsOverlapping(reservation.getStartDate(),
                        reservation.getEndDate(), r.getStartDate(), r.getEndDate()))
                .collect(Collectors.toList());

        return reservationsWithOverlappingDates.isEmpty() ||
                (reservationsWithOverlappingDates.size() == 1
                        && reservationsWithOverlappingDates.get(0).equals(reservation));
    }

    @Override
    public Optional<Room> assignRoom(Reservation reservation) {
        RoomType roomType = RoomType.getFittingSize(reservation.getNumberOfPeople());

        List<Room> rooms = roomRepository.findRoomsWithCapacity(roomType);

        Optional<Room> availableRoom = rooms.stream()
                .filter(room -> room.getReservations().stream()
                        .filter(bookedReservation ->
                                areIntervalsOverlapping(bookedReservation.getStartDate(),
                                        bookedReservation.getEndDate(),
                                        reservation.getStartDate(),
                                        reservation.getEndDate()))
                        .findAny()
                        .isEmpty())
                .findFirst();

        if (availableRoom.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(availableRoom.get());
    }

    private static boolean areIntervalsOverlapping(LocalDate start1, LocalDate end1,
                                                   LocalDate start2, LocalDate end2) {
        return !(end2.isBefore(start1) || start2.isAfter(end1));
    }
}