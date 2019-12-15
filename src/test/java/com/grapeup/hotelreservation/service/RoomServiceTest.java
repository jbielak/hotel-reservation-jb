package com.grapeup.hotelreservation.service;

import com.grapeup.hotelreservation.model.Reservation;
import com.grapeup.hotelreservation.model.Room;
import com.grapeup.hotelreservation.model.RoomType;
import com.grapeup.hotelreservation.repository.RoomRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RoomServiceTest {

    @MockBean
    private RoomRepository roomRepository;

    @Autowired
    private RoomService roomService;

    private static Reservation newReservation;
    private static Reservation reservation1;
    private static Reservation reservation2;
    private static Room roomId1;
    private static Room roomId2;

    @BeforeAll
    public static void setup() {
        newReservation = Reservation.builder().username("test")
                .numberOfPeople(3).startDate(LocalDate.of(2020, 8, 1))
                .endDate(LocalDate.of(2020, 9, 1)).build();

        reservation1 = Reservation.builder().username("test").id(1L).numberOfPeople(3).build();
        reservation2 = Reservation.builder().username("test").id(2L).numberOfPeople(3).build();
        roomId1 = Room.builder().id(1L).roomType(RoomType.BASIC)
                .reservations(new HashSet<>(List.of(reservation1))).build();
        roomId2 = Room.builder().id(1L).roomType(RoomType.BASIC)
                .reservations(new HashSet<>(List.of(reservation2))).build();
        reservation1.setRoom(roomId1);
        reservation2.setRoom(roomId2);
    }

    @Test
    public void shouldReturnAvailableRoomWhenOtherRoomsHaveReservationsOnTheSameIntervals() {
        reservation1.setStartDate(LocalDate.of(2020, 8, 1));
        reservation1.setEndDate(LocalDate.of(2020, 9, 1));

        reservation2.setStartDate(LocalDate.of(2020, 8, 1));
        reservation2.setEndDate(LocalDate.of(2020, 9, 1));

        Room roomId3 = Room.builder().id(3L).roomType(RoomType.BASIC)
                .reservations(new HashSet<>()).build();

        when(roomRepository.findRoomsWithCapacity(any()))
                .thenReturn(Arrays.asList(roomId1, roomId2, roomId3));
        Optional<Room> availableRoom = roomService.assignRoom(newReservation);

        assertThat(availableRoom.isPresent(), is(true));
        assertThat(availableRoom.get().getId(), is(3L));
    }

    @Test
    public void shouldReturnAvailableRoomWhenOtherRoomsHaveReservationsOnOverlappingIntervals() {
        reservation1.setStartDate(LocalDate.of(2020, 7, 1));
        reservation1.setEndDate(LocalDate.of(2020, 8, 1));

        reservation2.setStartDate(LocalDate.of(2020, 8, 1));
        reservation2.setEndDate(LocalDate.of(2020, 10, 1));

        Room roomId3 = Room.builder().id(3L).roomType(RoomType.BASIC)
                .reservations(new HashSet<>()).build();

        when(roomRepository.findRoomsWithCapacity(any())).thenReturn(Arrays.asList(roomId1, roomId2, roomId3));
        Optional<Room> availableRoom = roomService.assignRoom(newReservation);

        assertThat(availableRoom.isPresent(), is(true));
        assertThat(availableRoom.get().getId(), is(3L));
    }

    @Test
    public void shouldReturnAvailableRoomWhenOtherRoomsHaveReservationsNotOverlapping() {
        reservation1.setStartDate(LocalDate.of(2020, 7, 1));
        reservation1.setEndDate(LocalDate.of(2020, 7, 31));

        reservation2.setStartDate(LocalDate.of(2020, 9, 2));
        reservation2.setEndDate(LocalDate.of(2020, 10, 1));

        when(roomRepository.findRoomsWithCapacity(any())).thenReturn(Arrays.asList(roomId1, roomId2));
        Optional<Room> availableRoom = roomService.assignRoom(newReservation);

        assertThat(availableRoom.isPresent(), is(true));
        assertThat(availableRoom.get().getId(), is(1L));
    }

    @Test
    public void shouldNotReturnAvailableRoomWhenNoRoomsWithRequiredCapacity() {
        when(roomRepository.findRoomsWithCapacity(any())).thenReturn(Collections.EMPTY_LIST);

        Optional<Room> availableRoom = roomService.assignRoom(newReservation);

        assertThat(availableRoom.isEmpty(), is(true));
    }

    @Test
    public void shouldNotReturnAvailableRoomWhenAllDatesAreOverlapping() {
        reservation1.setStartDate(LocalDate.of(2020, 7, 1));
        reservation1.setEndDate(LocalDate.of(2020, 8, 2));

        when(roomRepository.findRoomsWithCapacity(any())).thenReturn(Arrays.asList(roomId1));
        Optional<Room> availableRoom = roomService.assignRoom(newReservation);

        assertThat(availableRoom.isEmpty(), is(true));
    }

    @Test
    public void shouldReturnTrueWhenDatesInCurrentRoomAreOverlappingOnlyWithUpdatingReservation() {
        reservation1.setStartDate(LocalDate.of(2020, 7, 1));
        reservation1.setEndDate(LocalDate.of(2020, 7, 31));

        assertThat(roomService.areDatesAvailableInCurrentRoom(reservation1),
                is(true));
    }

    @Test
    public void shouldReturnFalseWhenDatesInCurrentRoomAreOverlappingWithOtherReservation() {
        reservation1.setStartDate(LocalDate.of(2020, 7, 1));
        reservation1.setEndDate(LocalDate.of(2020, 7, 31));

        Reservation reservation3 = Reservation.builder()
                .username("test").id(3L).numberOfPeople(3)
                .startDate(LocalDate.of(2020, 9, 5))
                .endDate(LocalDate.of(2020, 9, 30)).build();
        roomId1.getReservations().add(reservation3);

        Reservation reservationToAdd = Reservation.builder()
                .username("test").id(4L).numberOfPeople(3)
                .startDate(LocalDate.of(2020, 9, 4))
                .endDate(LocalDate.of(2020, 9, 20))
                .room(roomId1).build();
        roomId1.getReservations().add(reservationToAdd);

        assertThat(roomService.areDatesAvailableInCurrentRoom(reservationToAdd), is(false));
    }

}