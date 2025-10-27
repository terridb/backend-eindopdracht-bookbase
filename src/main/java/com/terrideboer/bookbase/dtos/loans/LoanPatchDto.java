package com.terrideboer.bookbase.dtos.loans;

import com.terrideboer.bookbase.models.enums.LoanStatus;

import java.time.LocalDate;

public class LoanPatchDto {
    public LocalDate loanDate;
    public Integer loanPeriodInDays;
    public LoanStatus loanStatus;
    public Long bookCopyId;
    public Long userId;
}
