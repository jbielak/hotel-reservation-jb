package com.grapeup.hotelreservation.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = DatesValidator.class)
public @interface StartDateBeforeEnd {

    String message() default "Reservation start date should be before end date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
