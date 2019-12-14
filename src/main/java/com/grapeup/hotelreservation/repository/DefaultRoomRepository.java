package com.grapeup.hotelreservation.repository;

import com.grapeup.hotelreservation.model.Room;
import com.grapeup.hotelreservation.model.RoomType;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultRoomRepository implements RoomRepository {

    @Override
    public List<Room> findRoomsWithCapacity(RoomType roomType) {
        return null;
    }
    
}
