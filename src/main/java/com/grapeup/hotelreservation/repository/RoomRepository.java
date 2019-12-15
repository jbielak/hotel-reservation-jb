package com.grapeup.hotelreservation.repository;

import com.grapeup.hotelreservation.model.Room;
import com.grapeup.hotelreservation.model.RoomType;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends CrudRepository<Room, Long> {

    @Query("SELECT r FROM Room r WHERE r.roomType = :roomType")
    List<Room> findRoomsWithCapacity(@Param("roomType") RoomType roomType);
}
