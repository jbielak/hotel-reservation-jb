package com.grapeup.hotelreservation.dto;

import com.grapeup.hotelreservation.validator.StartDateBeforeEnd;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Future;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@StartDateBeforeEnd
public class ReservationDto {

    private Long id;

    private String username;

    private int numberOfPeople;

    @Future
    private LocalDate startDate;

    @Future
    private LocalDate endDate;

    private Long roomId;

}
