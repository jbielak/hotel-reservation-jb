package com.grapeup.hotelreservation.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

    @Getter
    @Setter
    public class Room {

        private Long id;
        private RoomType roomType;

        private Set<Reservation> reservations = new HashSet<>();

        public Room() {
        }

        public Room(RoomType roomType) {
            this.roomType = roomType;
        }

        public void addReservation(Reservation reservation) {
            reservations.add(reservation);
        }
    }

