package com.grapeup.hotelreservation.service;

import com.grapeup.hotelreservation.model.Reservation;
import com.grapeup.hotelreservation.model.Room;
import com.grapeup.hotelreservation.repository.RoomRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class DefaultRoomService implements RoomService {

    private RoomRepository roomRepository;

    public DefaultRoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Optional<Long> assignRoom(Reservation reservation) {
        List<Room> rooms = roomRepository.findRoomsWithCapacity(reservation.getNumberOfPeople());

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
        return Optional.of(availableRoom.get().getId());
    }

    @Override
    public Optional<Long> reassignRoom(Reservation reservation) {
        return null;
    }

    private static boolean areIntervalsOverlapping(LocalDate start1, LocalDate end1,
                                                   LocalDate start2, LocalDate end2) {
        return !(end2.isBefore(start1) || start2.isAfter(end1));
    }
}
