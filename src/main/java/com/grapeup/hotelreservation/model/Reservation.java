package com.grapeup.hotelreservation.model;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Reservation {

    private Long id;
    private String username;
    private int numberOfPeople;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long roomId;

}
