package com.grapeup.hotelreservation.service;

import com.grapeup.hotelreservation.model.Reservation;
import com.grapeup.hotelreservation.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ReservationServiceTest {

    @MockBean
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationService reservationService;

    private static Reservation mockReservation;

    @BeforeAll
    public static void setup() {
        mockReservation = Reservation.builder().id(1L).username("test")
                .numberOfPeople(3).startDate(LocalDate.of(2020, 8, 1))
                .endDate(LocalDate.of(2020, 9, 1))
                .roomId(1L).build();
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
                .roomId(1L).build();

        when(reservationRepository.findAll()).thenReturn(Arrays.asList(mockReservation, reservation2));

        List<Reservation> reservations = reservationService.findAll();

        assertThat(reservations, hasSize(2));
        assertThat(reservations.get(0), is(notNullValue()));
        assertThat(reservations.get(0).getId(), is(mockReservation.getId()));
        assertThat(reservations.get(0).getUsername(), is(mockReservation.getUsername()));
        assertThat(reservations.get(0).getNumberOfPeople(), is(mockReservation.getNumberOfPeople()));
        assertThat(reservations.get(0).getStartDate(), is(mockReservation.getStartDate()));
        assertThat(reservations.get(0).getEndDate(), is(mockReservation.getEndDate()));
        assertThat(reservations.get(0).getRoomId(), is(mockReservation.getRoomId()));
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
        assertThat(reservation.get().getRoomId(), is(mockReservation.getRoomId()));
    }

    @Test
    public void shouldReturnNoReservationForUnknownId() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Reservation> reservation = reservationService.findById(20L);

        assertThat(reservation.isEmpty(), is(true));
    }

}
