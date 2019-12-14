package com.grapeup.hotelreservation.repository;

import com.grapeup.hotelreservation.model.Room;
import java.util.List;

public interface RoomRepository {

    List<Room> findRoomsWithCapacity(int capacity);
}
