package com.terrideboer.bookbase.models;

import com.terrideboer.bookbase.models.enums.PaymentStatus;
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

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
}
