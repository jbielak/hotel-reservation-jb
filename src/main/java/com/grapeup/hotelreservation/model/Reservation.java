package com.grapeup.hotelreservation.model;

import java.time.LocalDate;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class Reservation {

    private Long id;

    private String username;

    private int numberOfPeople;

    private LocalDate startDate;

    private LocalDate endDate;

    private Room room;

    public Reservation(Long id, String username, int numberOfPeople,
                       LocalDate startDate, LocalDate endDate, Room room) {
        this.id = id;
        this.username = username;
        this.numberOfPeople = numberOfPeople;
        this.startDate = startDate;
        this.endDate = endDate;
        this.room = room;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
