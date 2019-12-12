package com.grapeup.hotelreservation.repository;

import com.grapeup.hotelreservation.model.Reservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long>  {

    List<Reservation> findAll();

    @Query("SELECT r FROM Reservation r WHERE r.room.id = :roomId")
    List<Reservation> findForRoom(@Param("roomId") Long roomId);
}
