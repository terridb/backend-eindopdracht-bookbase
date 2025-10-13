package com.terrideboer.bookbase.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    todo bookcopy
//    todo member
    private LocalDate loanDate;
    private Integer loanPeriodInDays;
}
