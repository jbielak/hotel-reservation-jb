package com.grapeup.hotelreservation.model;

import com.grapeup.hotelreservation.exception.IncorrectNumberOfPeopleException;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RoomTypeTest {

    @Test
    public void shouldGetBasicRoomTypeWhenRequiredValueIsSmaller() {
        RoomType roomType = RoomType.getFittingSize(2);
        assertThat(roomType, is(RoomType.BASIC));
    }
    @Test
    public void shouldGetBasicRoomTypeWhenRequiredValueIsEqual() {
        RoomType roomType = RoomType.getFittingSize(4);
        assertThat(roomType, is(RoomType.BASIC));
    }

    @Test
    public void shouldGetSuiteRoomTypeWhenRequiredValueIsSmaller() {
        RoomType roomType = RoomType.getFittingSize(5);
        assertThat(roomType, is(RoomType.SUITE));
    }
    @Test
    public void shouldGetSuiteRoomTypeWhenRequiredValueIsEqual() {
        RoomType roomType = RoomType.getFittingSize(6);
        assertThat(roomType, is(RoomType.SUITE));
    }
    @Test
    public void shouldGetPenthouseRoomTypeWhenRequiredValueIsSmaller() {
        RoomType roomType = RoomType.getFittingSize(7);
        assertThat(roomType, is(RoomType.PENTHOUSE));
    }
    @Test
    public void shouldGetPenthouseRoomTypeWhenRequiredValueIsEqual() {
        RoomType roomType = RoomType.getFittingSize(8);
        assertThat(roomType, is(RoomType.PENTHOUSE));
    }

    @Test
    public void shouldThrowIncorrectNumberOfPeopleExceptionWhenMoreThan8PlacesRequired() {
        assertThrows(IncorrectNumberOfPeopleException.class, () ->
                RoomType.getFittingSize(9));
    }

    @Test
    public void shouldThrowIncorrectNumberOfPeopleExceptionWhenLessThan1Passed() {
        assertThrows(IncorrectNumberOfPeopleException.class, () ->
                RoomType.getFittingSize(9));
    }
}
