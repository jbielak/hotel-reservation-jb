package com.grapeup.hotelreservation.repository;

import com.grapeup.hotelreservation.model.Room;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DefaultRoomRepository implements RoomRepository {
    @Override
    public List<Room> findRoomWithCapacity(int capacity) {
        return null;
    }
}
