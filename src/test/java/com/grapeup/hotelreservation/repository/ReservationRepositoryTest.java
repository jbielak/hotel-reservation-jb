package com.grapeup.hotelreservation.repository;

import com.grapeup.hotelreservation.model.Reservation;
import com.grapeup.hotelreservation.model.Room;
import com.grapeup.hotelreservation.model.RoomType;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
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
        assertThat(reservations, hasSize(4));
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

    @Test
    public void shouldCreateReservation() {
        Reservation newReservation = Reservation.builder()
                .username("new reservation")
                .numberOfPeople(8)
                .startDate(LocalDate.of(2022, 1, 5))
                .endDate(LocalDate.of(2022, 1, 15))
                .build();

        reservationRepository.save(newReservation);

        List<Reservation> reservations = reservationRepository.findAll();
        Optional<Reservation> createdReservation = reservationRepository.findById(5L);

        assertThat(reservations, hasSize(5));
        assertThat(createdReservation.isPresent(), is(true));
        assertThat(createdReservation.get().getId(), is(5L));
    }

    @Test
    public void shouldUpdateReservation() {
        Optional<Reservation> reservationToUpdate = reservationRepository.findById(4L);
        assertThat(reservationToUpdate.isPresent(), is(true));

        reservationToUpdate.get().setUsername("changed");
        reservationRepository.save(reservationToUpdate.get());

        List<Reservation> reservations = reservationRepository.findAll();
        Optional<Reservation> updatedReservation = reservationRepository.findById(4L);

        assertThat(reservations, hasSize(4));
        assertThat(updatedReservation.isPresent(), is(true));
        assertThat(updatedReservation.get().getId(), is(4L));
        assertThat(updatedReservation.get().getUsername(), is("changed"));
        assertThat(updatedReservation.get().getRoom(), notNullValue());
        assertThat(updatedReservation.get().getRoom().getId(), is(2L));
    }
}
