package com.grapeup.hotelreservation.repository;

import com.grapeup.hotelreservation.model.Reservation;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    List<Reservation> findAll();

    @Query("SELECT r FROM Reservation r WHERE r.room.id = :roomId")
    List<Reservation> findForRoom(Long roomId);
}
