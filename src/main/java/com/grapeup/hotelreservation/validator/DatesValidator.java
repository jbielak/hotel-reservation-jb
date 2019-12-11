package com.grapeup.hotelreservation.validator;

import com.grapeup.hotelreservation.model.Reservation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DatesValidator implements ConstraintValidator<StartDateBeforeEnd, Reservation> {

    @Override
    public void initialize(StartDateBeforeEnd constraintAnnotation) {
    }

    @Override
    public boolean isValid(Reservation reservation, ConstraintValidatorContext context) {
        return reservation.getStartDate().isBefore(reservation.getEndDate());
    }
}
