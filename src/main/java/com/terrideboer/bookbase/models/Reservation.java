package com.terrideboer.bookbase.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    todo bookcopy
//    todo member
    private LocalDate reservationDate;
//    todo reservationstatus
}
