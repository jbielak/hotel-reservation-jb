package com.grapeup.hotelreservation.service;

import com.grapeup.hotelreservation.model.Reservation;
import com.grapeup.hotelreservation.model.Room;
import com.grapeup.hotelreservation.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DefaultRoomService implements RoomService {

    private RoomRepository roomRepository;

    public DefaultRoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Optional<Long> assignRoom(Reservation reservation) {

        List<Room> rooms = roomRepository.findRoomWithCapacity(reservation.getNumberOfPeople());
        Room availableRoom = rooms.stream()
                .filter(room -> room.getReservations().stream()
                .filter(bookedReservation -> areIntervalsOverlappig(reservation.getStartDate(), reservation.getEndDate(),
                        bookedReservation.getStartDate(), bookedReservation.getEndDate())).findAny().isEmpty())
                .findFirst().get();

        return Optional.of(availableRoom.getId());

    }

    @Override
    public Optional<Long> reassignRoom(Reservation reservation) {
        return null;
    }

    private boolean areIntervalsOverlappig(LocalDate s1, LocalDate e1, LocalDate s2, LocalDate e2) {
        return !(e2.isBefore(s1) || s1.isAfter(e2));
    }
}
