package com.grapeup.hotelreservation.repository;

import com.grapeup.hotelreservation.model.Reservation;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
public class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    public void shouldFindAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();

        assertThat(reservations, is(not(empty())));
        assertThat(reservations, hasSize(3));
        assertThat(reservations.get(0).getId(), is(1L) );
        assertThat(reservations.get(0).getUsername(), is("test_user") );
        assertThat(reservations.get(0).getNumberOfPeople(), is(7) );
        assertThat(reservations.get(0).getStartDate().toString(), is("2020-12-17"));
        assertThat(reservations.get(0).getEndDate().toString(), is("2020-12-20"));
        assertThat(reservations.get(0).getRoom().getId(), is(1L));
    }

    @Test
    public void shouldFindReservationById() {
        Optional<Reservation> reservation = reservationRepository.findById(1L);

        assertThat(reservation.isPresent(), is(true));
        assertThat(reservation.get().getId(), is(1L) );
        assertThat(reservation.get().getUsername(), is("test_user") );
        assertThat(reservation.get().getNumberOfPeople(), is(7) );
        assertThat(reservation.get().getStartDate().toString(), is("2020-12-17"));
        assertThat(reservation.get().getEndDate().toString(), is("2020-12-20"));
        assertThat(reservation.get().getRoom().getId(), is(1L));
    }

    @Test
    public void shouldNotFindReservationById() {
        Optional<Reservation> reservation = reservationRepository.findById(99L);

        assertThat(reservation.isEmpty(), is(true));
    }

    @Test
    public void shouldFindAllReservationsForRoom() {
        List<Reservation> reservations = reservationRepository.findForRoom(1L);

        assertThat(reservations, is(not(empty())));
        assertThat(reservations, hasSize(2));
        assertThat(reservations.get(0).getId(), is(1L) );
        assertThat(reservations.get(0).getUsername(), is("test_user") );
        assertThat(reservations.get(0).getNumberOfPeople(), is(7) );
        assertThat(reservations.get(0).getStartDate().toString(), is("2020-12-17"));
        assertThat(reservations.get(0).getEndDate().toString(), is("2020-12-20"));
        assertThat(reservations.get(0).getRoom().getId(), is(1L));
    }

    @Test
    public void shouldNotFindReservationsForRoom() {
        List<Reservation> reservations = reservationRepository.findForRoom(3L);

        assertThat(reservations, is(empty()));
    }
}
