package com.grapeup.hotelreservation.integeration;

import com.grapeup.hotelreservation.TestUtils;
import com.grapeup.hotelreservation.dto.ReservationDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;
import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class HotelReservationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DataSource dataSource;

    /*public ConnectionHolder getConnectionHolder() {
        return () -> dataSource.getConnection();
    }*/

    @Test
    @DisplayName("GET /reservations - Success")
    public void shouldReturnAllReservations() throws Exception {
        mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].username", is("test_user")))
                .andExpect(jsonPath("$[0].numberOfPeople", is(7)))
                .andExpect(jsonPath("$[0].startDate", is("2020-12-17")))
                .andExpect(jsonPath("$[0].endDate", is("2020-12-20")))
                .andExpect(jsonPath("$[0].roomId", is(1)));
    }

    @Test
    @DisplayName("GET /reservations/1 - Found")
    void shouldGetReservationById() throws Exception {
        mockMvc.perform(get("/reservations/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.LOCATION, "/reservations/1"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("test_user")))
                .andExpect(jsonPath("$.numberOfPeople", is(7)))
                .andExpect(jsonPath("$.startDate", is("2020-12-17")))
                .andExpect(jsonPath("$.endDate", is("2020-12-20")))
                .andExpect(jsonPath("$.roomId", is(1)));
    }

    @Test
    @DisplayName("GET /reservations/1 - Not Found")
    void shouldNotFindReservationById() throws Exception {
        mockMvc.perform(get("/reservations/{id}", 99))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /reservations - Success")
    void shouldCreateReservation() throws Exception {
        ReservationDto postReservationDto = ReservationDto.builder().username("test")
                .numberOfPeople(3).startDate(LocalDate.of(2020, 8, 1))
                .endDate(LocalDate.of(2020, 9, 1)).build();

        mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(postReservationDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.LOCATION, "/reservations/5"))
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.username", is(postReservationDto.getUsername())))
                .andExpect(jsonPath("$.numberOfPeople", is(postReservationDto.getNumberOfPeople())))
                .andExpect(jsonPath("$.startDate", is(postReservationDto.getStartDate().toString())))
                .andExpect(jsonPath("$.endDate", is(postReservationDto.getEndDate().toString())))
                .andExpect(jsonPath("$.roomId", is(2)));
    }

    @Test
    @DisplayName("POST /reservations - Bad Request")
    void shouldReturnBadRequestWhenNoAvailableRoomForNewReservation() throws Exception {
        ReservationDto postReservationDto = ReservationDto.builder().username("test")
                .numberOfPeople(8).startDate(LocalDate.of(2020, 12, 16))
                .endDate(LocalDate.of(2020, 12, 20)).build();

        mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(postReservationDto)))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @DisplayName("POST /reservations - Bad Request")
    @ValueSource(ints = {-1, 20})
    void shouldReturnBadRequestWhenTooManyPeople(int number) throws Exception {
        ReservationDto postReservationDto = ReservationDto.builder().username("test")
                .numberOfPeople(number).startDate(LocalDate.of(2022, 12, 16))
                .endDate(LocalDate.of(2022, 12, 20)).build();

        mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(postReservationDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /reservations - Bad Request - start date in the past")
    void shouldReturnBadRequestWhenCreatingReservationWithStartDateInThePast() throws Exception {
        ReservationDto postReservationDto = ReservationDto.builder().username("test")
                .numberOfPeople(3).startDate(LocalDate.of(2018, 8, 1))
                .endDate(LocalDate.of(2023, 9, 1)).build();

        mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(postReservationDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("POST /reservations - Bad Request - end date in the past")
    void shouldReturnBadRequestWhenCreatingReservationWithEndDateInThePast() throws Exception {
        ReservationDto postReservationDto = ReservationDto.builder().username("test")
                .numberOfPeople(3).startDate(LocalDate.of(2023, 8, 1))
                .endDate(LocalDate.of(2018, 9, 1)).build();

        mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(postReservationDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("POST /reservations - Bad Request - end date before start date")
    void shouldReturnBadRequestWhenCreatingReservationWithEndDateBeforeStartDate() throws Exception {
        ReservationDto postReservationDto = ReservationDto.builder().username("test")
                .numberOfPeople(3).startDate(LocalDate.of(2024, 9, 1))
                .endDate(LocalDate.of(2024, 8, 1)).build();

        mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(postReservationDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("PUT /reservations/4 - Success")
    void shouldUpdateReservation() throws Exception {
        ReservationDto putReservationDto = ReservationDto.builder().username("updated_username")
                .numberOfPeople(4).startDate(LocalDate.of(2021, 12, 12))
                .endDate(LocalDate.of(2021, 12, 28))
                .roomId(1L).build();

        mockMvc.perform(put("/reservations/{id}", 4)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(putReservationDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.LOCATION, "/reservations/4"))
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.username", is("updated_username")))
                .andExpect(jsonPath("$.numberOfPeople", is(4)))
                .andExpect(jsonPath("$.startDate", is("2021-12-12")))
                .andExpect(jsonPath("$.endDate", is("2021-12-28")))
                .andExpect(jsonPath("$.roomId", is(2)));
    }

    @Test
    @DisplayName("PUT /reservations/4 - Success")
    void shouldUpdateReservationWhenNewDatesAreOverlappingWithOld() throws Exception {
        ReservationDto putReservationDto = ReservationDto.builder().username("test_user")
                .numberOfPeople(3).startDate(LocalDate.of(2021, 12, 24))
                .endDate(LocalDate.of(2021, 12, 30))
                .roomId(1L).build();

        mockMvc.perform(put("/reservations/{id}", 4)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(putReservationDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.LOCATION, "/reservations/4"))
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.username", is("test_user")))
                .andExpect(jsonPath("$.numberOfPeople", is(3)))
                .andExpect(jsonPath("$.startDate", is("2021-12-24")))
                .andExpect(jsonPath("$.endDate", is("2021-12-30")))
                .andExpect(jsonPath("$.roomId", is(2)));
    }

    @Test
    @DisplayName("PUT /reservations/1 - Success - assign smaller room")
    void shouldUpdateReservationWhenSmallerRoomRequired() throws Exception {
        ReservationDto putReservationDto = ReservationDto.builder().username("test_user")
                .numberOfPeople(3).startDate(LocalDate.of(2022, 12, 24))
                .endDate(LocalDate.of(2022, 12, 30))
                .roomId(1L).build();

        mockMvc.perform(put("/reservations/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(putReservationDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.LOCATION, "/reservations/1"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("test_user")))
                .andExpect(jsonPath("$.numberOfPeople", is(3)))
                .andExpect(jsonPath("$.startDate", is("2022-12-24")))
                .andExpect(jsonPath("$.endDate", is("2022-12-30")))
                .andExpect(jsonPath("$.roomId", is(2)));
    }

    @Test
    @DisplayName("PUT /reservations/1 - Bad Request - not available room")
    void shouldReturnBadRequestWhenNoAvailableRoomForUpdatedReservation() throws Exception {
        ReservationDto putReservationDto = ReservationDto.builder().id(1L).username("test")
                .numberOfPeople(5).startDate(LocalDate.of(2021, 12, 24))
                .endDate(LocalDate.of(2021, 12, 27))
                .roomId(1L).build();

        mockMvc.perform(put("/reservations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(putReservationDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /reservations/1 - Bad Request - start date in the past")
    void shouldReturnBadRequestWhenUpdatingReservationWithStartDateInThePast() throws Exception {
        ReservationDto putReservationDto = ReservationDto.builder().username("test")
                .numberOfPeople(3).startDate(LocalDate.of(2018, 8, 1))
                .endDate(LocalDate.of(2023, 9, 1))
                .roomId(1L).build();

        mockMvc.perform(put("/reservations/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(putReservationDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("PUT /reservations/1 - Bad Request - end date in the past")
    void shouldReturnBadRequestWhenUpdatingReservationWithEndDateInThePast() throws Exception {
        ReservationDto putReservationDto = ReservationDto.builder().username("test")
                .numberOfPeople(3).startDate(LocalDate.of(2023, 8, 1))
                .endDate(LocalDate.of(2018, 9, 1))
                .roomId(1L).build();

        mockMvc.perform(put("/reservations/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(putReservationDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("PUT /reservations/1 - Bad Request - end date before start")
    void shouldReturnBadRequestWhenUpdatingReservationWithEndDateBeforeStartDate() throws Exception {
        ReservationDto putReservationDto = ReservationDto.builder().username("test")
                .numberOfPeople(3).startDate(LocalDate.of(2024, 9, 1))
                .endDate(LocalDate.of(2024, 8, 1))
                .roomId(1L).build();

        mockMvc.perform(put("/reservations/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(putReservationDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("PUT /reservations/99 - Not Found")
    void shouldNotUpdateReservationWhenNotExist() throws Exception {
        ReservationDto putReservationDto = ReservationDto.builder().username("test")
                .numberOfPeople(3).startDate(LocalDate.of(2020, 8, 1))
                .endDate(LocalDate.of(2020, 9, 1))
                .roomId(1L).build();

        mockMvc.perform(put("/reservations/{id}", 99)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(putReservationDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /reservations/1 - Success")
    void shouldDeleteReservation() throws Exception {
        mockMvc.perform(delete("/reservations/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /reservations?roomNumber=5 - Success - empty list")
    void shouldReturnEmptyListWhenNoReservationsForRoom() throws Exception {
        mockMvc.perform(get("/reservations?roomNumber={roomNumber}", 5))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("GET /reservations?roomNumber=1 - Success")
    void shouldReturnReservationsForRoom() throws Exception {
        mockMvc.perform(get("/reservations?roomNumber={roomNumber}", 1l))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].username", is("test_user")))
                .andExpect(jsonPath("$[0].numberOfPeople", is(7)))
                .andExpect(jsonPath("$[0].startDate", is("2020-12-17")))
                .andExpect(jsonPath("$[0].endDate", is("2020-12-20")))
                .andExpect(jsonPath("$[0].roomId", is(1)));
    }
}
