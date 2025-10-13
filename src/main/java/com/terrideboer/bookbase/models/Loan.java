package com.terrideboer.bookbase.models;

import com.terrideboer.bookbase.models.enums.LoanStatus;
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

    @Enumerated(EnumType.STRING)
    private LoanStatus loanStatus;
}
