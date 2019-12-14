package com.grapeup.hotelreservation.repository;

import com.grapeup.hotelreservation.model.Room;
import com.grapeup.hotelreservation.model.RoomType;
import java.util.List;

public interface RoomRepository {

    List<Room> findRoomsWithCapacity(RoomType roomType);
}
