package com.grapeup.hotelreservation.service;

import com.grapeup.hotelreservation.model.Reservation;
import com.grapeup.hotelreservation.model.Room;
import com.grapeup.hotelreservation.model.RoomType;
import com.grapeup.hotelreservation.repository.RoomRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
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

    @BeforeAll
    public static void setup() {
        newReservation = Reservation.builder().username("test")
                .numberOfPeople(3).startDate(LocalDate.of(2020, 8, 1))
                .endDate(LocalDate.of(2020, 9, 1)).build();
    }

    @Test
    public void shouldReturnAvailableRoomWhenOtherRoomsHaveReservationsOnTheSameIntervals() {
        Reservation reservation = Reservation.builder().username("test").id(1L)
                .numberOfPeople(3).startDate(LocalDate.of(2020, 8, 1))
                .endDate(LocalDate.of(2020, 9, 1)).roomId(1L).build();
        Reservation reservation2 = Reservation.builder().username("test").id(1L)
                .numberOfPeople(3).startDate(LocalDate.of(2020, 8, 1))
                .endDate(LocalDate.of(2020, 9, 1)).roomId(3L).build();

        Room room1 = new Room();
        room1.setId(1L);
        room1.setRoomType(RoomType.BASIC);
        room1.setReservations(new HashSet(Arrays.asList(reservation)));
        Room room2 = new Room();
        room2.setId(2L);
        room2.setRoomType(RoomType.BASIC);
        room2.setReservations(new HashSet(Arrays.asList(reservation2)));
        Room room3 = new Room();
        room3.setId(3L);
        room3.setRoomType(RoomType.BASIC);

        when(roomRepository.findRoomsWithCapacity(anyInt())).thenReturn(Arrays.asList(room1, room2, room3));
        Optional<Long> availableRoom = roomService.assignRoom(newReservation);

        assertThat(availableRoom.isPresent(), is(true));
        assertThat(availableRoom.get(), is(3L));
    }

    @Test
    public void shouldReturnAvailableRoomWhenOtherRoomsHaveReservationsOnOverlappingIntervals() {
        Reservation reservation = Reservation.builder().username("test").id(1L)
                .numberOfPeople(3).startDate(LocalDate.of(2020, 7, 1))
                .endDate(LocalDate.of(2020, 8, 2)).roomId(1L).build();
        Reservation reservation2 = Reservation.builder().username("test").id(1L)
                .numberOfPeople(3).startDate(LocalDate.of(2020, 8, 31))
                .endDate(LocalDate.of(2020, 10, 1)).roomId(3L).build();

        Room room1 = new Room();
        room1.setId(1L);
        room1.setRoomType(RoomType.BASIC);
        room1.setReservations(new HashSet(Arrays.asList(reservation)));
        Room room2 = new Room();
        room2.setId(2L);
        room2.setRoomType(RoomType.BASIC);
        room2.setReservations(new HashSet(Arrays.asList(reservation2)));
        Room room3 = new Room();
        room3.setId(3L);
        room3.setRoomType(RoomType.BASIC);

        when(roomRepository.findRoomsWithCapacity(anyInt())).thenReturn(Arrays.asList(room1, room2, room3));
        Optional<Long> availableRoom = roomService.assignRoom(newReservation);

        assertThat(availableRoom.isPresent(), is(true));
        assertThat(availableRoom.get(), is(3L));
    }

    @Test
    public void shouldReturnAvailableRoomWhenOtherRoomsHaveReservationsDontOverlap() {
        Reservation reservation = Reservation.builder().username("test").id(1L)
                .numberOfPeople(3).startDate(LocalDate.of(2020, 7, 1))
                .endDate(LocalDate.of(2020, 7, 31)).roomId(1L).build();
        Reservation reservation2 = Reservation.builder().username("test").id(1L)
                .numberOfPeople(3).startDate(LocalDate.of(2020, 9, 2))
                .endDate(LocalDate.of(2020, 10, 1)).roomId(3L).build();

        Room room1 = new Room();
        room1.setId(1L);
        room1.setRoomType(RoomType.BASIC);
        room1.setReservations(new HashSet(Arrays.asList(reservation)));
        Room room2 = new Room();
        room2.setId(2L);
        room2.setRoomType(RoomType.BASIC);
        room2.setReservations(new HashSet(Arrays.asList(reservation2)));

        when(roomRepository.findRoomsWithCapacity(anyInt())).thenReturn(Arrays.asList(room1, room2));
        Optional<Long> availableRoom = roomService.assignRoom(newReservation);

        assertThat(availableRoom.isPresent(), is(true));
        assertThat(availableRoom.get(), is(1L));
    }

    @Test
    public void shouldNotReturnAvailableRoomWhenNoRoomsWithRequiredCapacity() {
        Reservation newReservation = Reservation.builder().username("test")
                .numberOfPeople(3).startDate(LocalDate.of(2020, 8, 1))
                .endDate(LocalDate.of(2020, 9, 1)).build();

        when(roomRepository.findRoomsWithCapacity(anyInt())).thenReturn(Collections.EMPTY_LIST);

        Optional<Long> availableRoom = roomService.assignRoom(newReservation);

        assertThat(availableRoom.isEmpty(), is(true));
    }

    @Test
    public void shouldNotReturnAvailableRoomWhenAllDatesAreOverlapping() {
        Reservation reservation = Reservation.builder().username("test").id(1L)
                .numberOfPeople(3).startDate(LocalDate.of(2020, 7, 1))
                .endDate(LocalDate.of(2020, 8, 2)).roomId(1L).build();

        Room room1 = new Room();
        room1.setId(1L);
        room1.setRoomType(RoomType.BASIC);
        room1.setReservations(new HashSet(Arrays.asList(reservation)));

        when(roomRepository.findRoomsWithCapacity(anyInt())).thenReturn(Arrays.asList(room1));
        Optional<Long> availableRoom = roomService.assignRoom(newReservation);

        assertThat(availableRoom.isEmpty(), is(true));
    }

}
