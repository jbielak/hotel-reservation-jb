package com.grapeup.hotelreservation.controller;

import com.grapeup.hotelreservation.model.Reservation;
import com.grapeup.hotelreservation.service.ReservationService;
import java.time.LocalDate;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ReservationControllerTest {

    @MockBean
    private ReservationService reservationService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /reservations - Success - empty list")
    public void shouldReturnEmptyListWhenNoReservations() throws Exception {
        mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("GET /reservations - Success")
    public void shouldReturnAllReservations() throws Exception {
        Reservation mockReservation1 = Reservation.builder().id(1L).username("test")
                .numberOfPeople(3).startDate(LocalDate.of(2020, 8, 1))
                .endDate(LocalDate.of(2020, 9, 1))
                .roomId(1L).build();

        Reservation mockReservation2 = Reservation.builder().id(2L).username("test")
                .numberOfPeople(5).startDate(LocalDate.of(2020, 7, 1))
                .endDate(LocalDate.of(2020, 9, 1))
                .roomId(2L).build();

        when(reservationService.findAll()).thenReturn(Arrays.asList(mockReservation1, mockReservation2));

        mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(mockReservation1.getId().intValue())))
                .andExpect(jsonPath("$[0].username", is(mockReservation1.getUsername())))
                .andExpect(jsonPath("$[0].numberOfPeople", is(mockReservation1.getNumberOfPeople())))
                .andExpect(jsonPath("$[0].startDate", is(mockReservation1.getStartDate().toString())))
                .andExpect(jsonPath("$[0].endDate", is(mockReservation1.getEndDate().toString())))
                .andExpect(jsonPath("$[0].roomId", is(mockReservation1.getRoomId().intValue())));

    }
}
