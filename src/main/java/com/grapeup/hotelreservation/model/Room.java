package com.grapeup.hotelreservation.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "room")
    @Getter
    @Setter
    @NoArgsConstructor
    @Builder
    public class Room {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Getter
        private Long id;

        @Getter
        @Enumerated(EnumType.STRING)
        @Column(name = "room_type")
        private RoomType roomType;

        @OneToMany(cascade = CascadeType.ALL,
                fetch = FetchType.LAZY,
                mappedBy = "room")
        @Getter
        private Set<Reservation> reservations = new HashSet<>();

    public Room(Long id, RoomType roomType, Set<Reservation> reservations) {
        this.id = id;
        this.roomType = roomType;
        this.reservations = reservations;
    }

    public Room(RoomType roomType) {
            this.roomType = roomType;
        }

        public void addReservation(Reservation reservation) {
            reservations.add(reservation);
        }
    }

