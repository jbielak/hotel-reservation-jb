package com.grapeup.hotelreservation.service;

import com.grapeup.hotelreservation.exception.AvailableRoomNotFoundException;
import com.grapeup.hotelreservation.model.Reservation;
import com.grapeup.hotelreservation.model.Room;
import com.grapeup.hotelreservation.model.RoomType;
import com.grapeup.hotelreservation.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ReservationServiceTest {

    @MockBean
    private ReservationRepository reservationRepository;

    @MockBean
    private RoomService roomService;

    @Autowired
    private ReservationService reservationService;

    private static Reservation mockReservation;
    private static Room mockRoom;

    @BeforeAll
    public static void setup() {
        mockReservation = Reservation.builder().id(1L).username("test")
                .numberOfPeople(3).startDate(LocalDate.of(2020, 8, 1))
                .endDate(LocalDate.of(2020, 9, 1)).build();
        mockRoom = Room.builder().id(1L).roomType(RoomType.BASIC)
                .reservations(new HashSet<>(Arrays.asList(mockReservation))).build();
        mockReservation.setRoom(mockRoom);
    }

    @Test
    public void shouldReturnEmptyListWhenNoReservations() {
        when(reservationRepository.findAll()).thenReturn(Collections.emptyList());

        List<Reservation> reservations = reservationService.findAll();

        assertThat(reservations, hasSize(0));
    }

    @Test
    public void shouldReturnReservations() {
        Reservation reservation2 = Reservation.builder().id(2L).username("test")
                .numberOfPeople(3).startDate(LocalDate.of(2020, 8, 1))
                .endDate(LocalDate.of(2021, 9, 1))
                .room(mockRoom).build();

        when(reservationRepository.findAll()).thenReturn(Arrays.asList(mockReservation, reservation2));

        List<Reservation> reservations = reservationService.findAll();

        assertThat(reservations, hasSize(2));
        assertThat(reservations.get(0), is(notNullValue()));
        assertThat(reservations.get(0).getId(), is(mockReservation.getId()));
        assertThat(reservations.get(0).getUsername(), is(mockReservation.getUsername()));
        assertThat(reservations.get(0).getNumberOfPeople(), is(mockReservation.getNumberOfPeople()));
        assertThat(reservations.get(0).getStartDate(), is(mockReservation.getStartDate()));
        assertThat(reservations.get(0).getEndDate(), is(mockReservation.getEndDate()));
        assertThat(reservations.get(0).getRoom().getId(), is(mockReservation.getRoom().getId()));
    }

    @Test
    public void shouldReturnReservationForId() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(mockReservation));

        Optional<Reservation> reservation = reservationService.findById(1L);

        assertThat(reservation.isPresent(), is(true));
        assertThat(reservation.get().getId(), is(mockReservation.getId()));
        assertThat(reservation.get().getUsername(), is(mockReservation.getUsername()));
        assertThat(reservation.get().getNumberOfPeople(), is(mockReservation.getNumberOfPeople()));
        assertThat(reservation.get().getStartDate(), is(mockReservation.getStartDate()));
        assertThat(reservation.get().getEndDate(), is(mockReservation.getEndDate()));
        assertThat(reservation.get().getRoom().getId(), is(mockReservation.getRoom().getId()));
    }

    @Test
    public void shouldReturnNoReservationForUnknownId() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Reservation> reservation = reservationService.findById(20L);

        assertThat(reservation.isEmpty(), is(true));
    }

    @Test
    public void shouldSaveReservationThenReturnReservationWithAssignedIdAndRoom() {
        Reservation reservationToSave = Reservation.builder().username("test")
                .numberOfPeople(3).startDate(LocalDate.of(2020, 8, 1))
                .endDate(LocalDate.of(2020, 9, 1)).build();

        when(roomService.assignRoom(reservationToSave)).thenReturn(Optional.of(mockRoom));

        doAnswer(invocation -> {
            ReflectionTestUtils.setField((Reservation) invocation.getArgument(0), "id", 1L);
            return reservationToSave;
        }).when(reservationRepository).save(reservationToSave);

        Reservation savedReservation = reservationService.save(reservationToSave);

        assertThat(savedReservation, is(notNullValue()));
        assertThat(savedReservation.getId(), is(1L));
        assertThat(savedReservation.getId(), is(notNullValue()));
        assertThat(savedReservation.getRoom(), is(notNullValue()));
        assertThat(savedReservation.getRoom().getId(), is(notNullValue()));
        assertThat(savedReservation.getRoom().getId(), is(1L));
        assertThat(savedReservation.getUsername(), is(reservationToSave.getUsername()));
        assertThat(savedReservation.getNumberOfPeople(), is(reservationToSave.getNumberOfPeople()));
        assertThat(savedReservation.getStartDate(), is(reservationToSave.getStartDate()));
        assertThat(savedReservation.getEndDate(), is(reservationToSave.getEndDate()));
    }

    @Test
    public void shouldThrowNoAvailableRoomExceptionWhenNoAvailableRoomForNewReservation() {
        Reservation reservationToSave = Reservation.builder().username("test")
                .numberOfPeople(3).startDate(LocalDate.of(2020, 8, 1))
                .endDate(LocalDate.of(2020, 9, 1)).build();

        when(roomService.assignRoom(reservationToSave)).thenReturn(Optional.empty());

        assertThrows(AvailableRoomNotFoundException.class, () ->
                reservationService.save(reservationToSave));
    }

    @Test
    public void shouldUpdateReservationThenReturnReservationWithReassignedRoomId() {
        Reservation reservationToUpdate = Reservation.builder().id(1L).username("test")
                .numberOfPeople(5).startDate(LocalDate.of(2020, 8, 1))
                .endDate(LocalDate.of(2020, 9, 1)).build();
        Room newRoom = Room.builder().id(2L).roomType(RoomType.SUITE).build();

        when(roomService.assignRoom(reservationToUpdate)).thenReturn(Optional.of(newRoom));
        when(reservationRepository.save(reservationToUpdate)).thenReturn(reservationToUpdate);
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(mockReservation));

        Reservation updatedReservation =
                reservationService.update(reservationToUpdate, mockReservation).get();

        assertThat(updatedReservation, is(notNullValue()));
        assertThat(updatedReservation.getId(), is(notNullValue()));
        assertThat(updatedReservation.getRoom(), is(notNullValue()));
        assertThat(updatedReservation.getRoom().getId(), is(notNullValue()));
        assertThat(updatedReservation.getRoom().getId(), is(2L));
        assertThat(updatedReservation.getUsername(), is(reservationToUpdate.getUsername()));
        assertThat(updatedReservation.getNumberOfPeople(), is(reservationToUpdate.getNumberOfPeople()));
        assertThat(updatedReservation.getStartDate(), is(reservationToUpdate.getStartDate()));
        assertThat(updatedReservation.getEndDate(), is(reservationToUpdate.getEndDate()));
    }

    @Test
    public void shouldUpdateReservationWithoutAssigningNewRoomType() {
        Reservation reservationToUpdate = Reservation.builder().id(1L).username("test")
                .numberOfPeople(4).startDate(LocalDate.of(2020, 8, 1))
                .endDate(LocalDate.of(2020, 9, 1))
                .room(mockRoom).build();

        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(mockReservation));
        when(reservationRepository.save(reservationToUpdate)).thenReturn(reservationToUpdate);

        Reservation updatedReservation =
                reservationService.update(reservationToUpdate, mockReservation).get();

        assertThat(updatedReservation, is(notNullValue()));
        assertThat(updatedReservation.getId(), is(notNullValue()));
        assertThat(updatedReservation.getRoom(), is(notNullValue()));
        assertThat(updatedReservation.getRoom().getId(), is(notNullValue()));
        assertThat(updatedReservation.getRoom().getId(), is(1L));
        assertThat(updatedReservation.getUsername(), is(reservationToUpdate.getUsername()));
        assertThat(updatedReservation.getNumberOfPeople(), is(reservationToUpdate.getNumberOfPeople()));
        assertThat(updatedReservation.getStartDate(), is(reservationToUpdate.getStartDate()));
        assertThat(updatedReservation.getEndDate(), is(reservationToUpdate.getEndDate()));

        verify(roomService, times(0)).assignRoom(any());
    }

    @Test
    public void shouldUpdateReservationWithoutAssigningNewRoomTypeWhenNewDatesAreAvailable() {
        Reservation reservationToUpdate = Reservation.builder().id(1L).username("test")
                .numberOfPeople(4).startDate(LocalDate.of(2020, 8, 2))
                .endDate(LocalDate.of(2020, 9, 2))
                .room(mockRoom).build();

        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(mockReservation));
        when(reservationRepository.save(reservationToUpdate)).thenReturn(reservationToUpdate);
        when(roomService.areDatesAvailableInCurrentRoom(any())).thenReturn(true);

        Reservation updatedReservation =
                reservationService.update(reservationToUpdate, mockReservation).get();

        assertThat(updatedReservation, is(notNullValue()));
        assertThat(updatedReservation.getId(), is(notNullValue()));
        assertThat(updatedReservation.getRoom(), is(notNullValue()));
        assertThat(updatedReservation.getRoom().getId(), is(notNullValue()));
        assertThat(updatedReservation.getRoom().getId(), is(1L));
        assertThat(updatedReservation.getUsername(), is(reservationToUpdate.getUsername()));
        assertThat(updatedReservation.getNumberOfPeople(), is(reservationToUpdate.getNumberOfPeople()));
        assertThat(updatedReservation.getStartDate(), is(reservationToUpdate.getStartDate()));
        assertThat(updatedReservation.getEndDate(), is(reservationToUpdate.getEndDate()));

        verify(roomService, times(0)).assignRoom(any());
    }

    @Test
    public void shouldThrowNoAvailableRoomExceptionWhenNoAvailableRoomForUpdatedReservation() {
        Reservation reservationToUpdate = Reservation.builder().id(1L).username("test")
                .numberOfPeople(5).startDate(LocalDate.of(2020, 8, 1))
                .endDate(LocalDate.of(2020, 9, 1))
                .room(mockRoom).build();

        when(roomService.assignRoom(reservationToUpdate)).thenReturn(Optional.empty());

        assertThrows(AvailableRoomNotFoundException.class, () ->
                reservationService.save(reservationToUpdate));
    }

    @Test
    public void shouldDeleteReservationById() {
        reservationService.delete(any());
        verify(reservationRepository, times(1)).deleteById(any());
    }

    @Test
    public void shouldReturnEmptyListOfReservationsForRoomWithNoReservations() {
        when(reservationRepository.findForRoom(anyLong())).thenReturn(Collections.EMPTY_LIST);

        List<Reservation> reservations = reservationService.findForRoom(1L);

        assertThat(reservations, is(notNullValue()));
        assertThat(reservations, hasSize(0));
    }

    @Test
    public void shouldReturnEListOfReservationsForRoom() {
        Reservation reservation2 = Reservation.builder().id(2L).username("test")
                .numberOfPeople(3).startDate(LocalDate.of(2020, 8, 1))
                .endDate(LocalDate.of(2021, 9, 1))
                .room(mockRoom).build();
        when(reservationRepository.findForRoom(anyLong())).thenReturn(Arrays.asList(mockReservation, reservation2));

        List<Reservation> reservations = reservationService.findForRoom(1L);

        assertThat(reservations, is(notNullValue()));
        assertThat(reservations, hasSize(2));
    }

}
