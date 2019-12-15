package com.grapeup.hotelreservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No rooms available")
public class AvailableRoomNotFoundException extends IllegalStateException {

}
