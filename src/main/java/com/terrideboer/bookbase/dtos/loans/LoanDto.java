package com.terrideboer.bookbase.dtos.loans;

import com.terrideboer.bookbase.models.enums.LoanStatus;

import java.time.LocalDate;

public class LoanDto {
    public Long id;
    public LocalDate loanDate;
    public Integer loanPeriodInDays;
    public LoanStatus loanStatus;

    //    todo userid en bookcopyid mappers
    public Long bookCopyId;
    public Long userId;
}
