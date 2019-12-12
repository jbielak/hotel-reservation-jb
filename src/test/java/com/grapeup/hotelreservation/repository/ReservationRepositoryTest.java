package com.grapeup.hotelreservation.repository;

import com.grapeup.hotelreservation.model.Reservation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsNot.not;

@DataJpaTest
public class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    public void shouldFindAll() {
        List<Reservation> reservations = reservationRepository.findAll();

        assertThat(reservations, is(not(empty())));
        assertThat(reservations, hasSize(2));
        assertThat(reservations.get(0).getId(), is(1L) );
        assertThat(reservations.get(0).getUsername(), is("test_user") );
        assertThat(reservations.get(0).getNumberOfPeople(), is(6) );
        assertThat(reservations.get(0).getStartDate().toString(), is("2019-12-14"));
        assertThat(reservations.get(0).getEndDate().toString(), is("2019-12-20"));
        assertThat(reservations.get(0).getRoomId(), is(1L));
    }
}
