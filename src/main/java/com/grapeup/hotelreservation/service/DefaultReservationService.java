package com.grapeup.hotelreservation.service;

import com.grapeup.hotelreservation.exception.AvailableRoomNotFoundException;
import com.grapeup.hotelreservation.model.Reservation;
import com.grapeup.hotelreservation.model.Room;
import com.grapeup.hotelreservation.model.RoomType;
import com.grapeup.hotelreservation.repository.ReservationRepository;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultReservationService  implements ReservationService {

    private ReservationRepository reservationRepository;

    private RoomService roomService;

    public DefaultReservationService(ReservationRepository reservationRepository,
                                     RoomService roomService) {
        this.reservationRepository = reservationRepository;
        this.roomService = roomService;
    }

    @Override
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }

    @Override
    public Reservation save(Reservation reservation) {

        Optional<Room> availableRoom = roomService.assignRoom(reservation);
        if (availableRoom.isPresent()) {
            reservation.setRoom(availableRoom.get());
        } else {
            throw new AvailableRoomNotFoundException();
        }
        return reservationRepository.save(reservation);
    }

    @Override
    public Optional<Reservation> update(Reservation reservation, Reservation existingReservation) {

        if (onlyNumberOfPeopleChanged(reservation, existingReservation)
        || roomService.areDatesAvailableInCurrentRoom(reservation)) {
            return Optional.of(reservationRepository.save(reservation));
        }

        Optional<Room> availableRoom = roomService.assignRoom(reservation);

        if (availableRoom.isPresent()) {
            reservation.setRoom(availableRoom.get());
        } else {
            throw new AvailableRoomNotFoundException();
        }
        return Optional.of(reservationRepository.save(reservation));
    }

    @Override
    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }

    @Override
    public List<Reservation> findForRoom(Long roomId) {
        return reservationRepository.findForRoom(roomId);
    }

    private static boolean onlyNumberOfPeopleChanged(Reservation updated,
                                                     Reservation existingReservation) {
        return !shouldAssignDifferentTypeOfRoom(updated, existingReservation.getNumberOfPeople())
                && areDatesSame(updated, existingReservation.getStartDate(),
                existingReservation.getEndDate());
    }

    private static boolean shouldAssignDifferentTypeOfRoom(Reservation updated,
                                                           int numberOfPeople) {
        RoomType requiredRoomType = RoomType.getFittingSize(updated.getNumberOfPeople());
        RoomType currentRoomType = RoomType.getFittingSize(numberOfPeople);

        return requiredRoomType != currentRoomType;
    }

    private static boolean areDatesSame(Reservation updated,
                                           LocalDate start, LocalDate end) {
        return updated.getStartDate().isEqual(start)
                && updated.getEndDate().isEqual(end);
    }
}
