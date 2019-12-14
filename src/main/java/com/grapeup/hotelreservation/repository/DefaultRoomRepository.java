package com.grapeup.hotelreservation.repository;

import com.grapeup.hotelreservation.model.Room;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultRoomRepository implements RoomRepository {
    @Override
    public List<Room> findRoomsWithCapacity(int capacity) {
        return null;
    }
}
