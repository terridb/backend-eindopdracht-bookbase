package com.terrideboer.bookbase.dtos.loans;

import com.terrideboer.bookbase.models.enums.LoanStatus;

import java.time.LocalDate;

public class LoanSummaryDto {
    public Long id;
    public LocalDate loanDate;
    public LocalDate returnDate;
    public Integer loanPeriodInDays;
    public LoanStatus loanStatus;
}
