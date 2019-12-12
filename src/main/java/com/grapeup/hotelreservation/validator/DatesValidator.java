package com.grapeup.hotelreservation.validator;

import com.grapeup.hotelreservation.dto.ReservationDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DatesValidator implements ConstraintValidator<StartDateBeforeEnd, ReservationDto> {

    @Override
    public void initialize(StartDateBeforeEnd constraintAnnotation) {
    }

    @Override
    public boolean isValid(ReservationDto reservationDto, ConstraintValidatorContext context) {
        return reservationDto.getStartDate().isBefore(reservationDto.getEndDate());
    }
}
