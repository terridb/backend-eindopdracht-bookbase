package com.terrideboer.bookbase.models;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "fines")
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    todo loan
    private BigDecimal fineAmount;
//    todo payment status
}
