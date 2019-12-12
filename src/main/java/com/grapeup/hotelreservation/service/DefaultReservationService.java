package com.grapeup.hotelreservation.service;

import com.grapeup.hotelreservation.exception.AvailableRoomNotFoundException;
import com.grapeup.hotelreservation.model.Reservation;
import com.grapeup.hotelreservation.repository.ReservationRepository;
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

        Optional<Long> availableRoom = roomService.assignRoom(reservation);
        if (availableRoom.isPresent()) {
            reservation.setRoomId(availableRoom.get());
        } else {
            throw new AvailableRoomNotFoundException();
        }
        return reservationRepository.save(reservation);
    }

    @Override
    public Optional<Reservation> update(Reservation reservation) {
        Optional<Long> availableRoom = roomService.reassignRoom(reservation);

        if (availableRoom.isPresent()) {
            reservation.setRoomId(availableRoom.get());
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
}
