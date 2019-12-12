package com.grapeup.hotelreservation.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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

    private Long roomId;

    public Reservation(Long id, String username, int numberOfPeople,
                       LocalDate startDate, LocalDate endDate, Long roomId) {
        this.id = id;
        this.username = username;
        this.numberOfPeople = numberOfPeople;
        this.startDate = startDate;
        this.endDate = endDate;
        this.roomId = roomId;
    }
}
