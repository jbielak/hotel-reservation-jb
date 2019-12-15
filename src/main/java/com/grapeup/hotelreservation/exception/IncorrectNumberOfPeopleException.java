package com.grapeup.hotelreservation.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Incorrect number of people" )
@NoArgsConstructor
public class IncorrectNumberOfPeopleException extends IllegalArgumentException {

    public IncorrectNumberOfPeopleException(String s) {
        super(s);
    }
}
