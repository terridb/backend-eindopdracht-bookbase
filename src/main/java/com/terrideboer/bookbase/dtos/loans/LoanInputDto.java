package com.terrideboer.bookbase.dtos.loans;

import com.terrideboer.bookbase.models.enums.LoanStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class LoanInputDto {

    @PastOrPresent(message = "Loan date cannot be in the future")
    public LocalDate loanDate;

    @Positive(message = "Loan period in days must be a positive number")
    public Integer loanPeriodInDays;

    public LoanStatus loanStatus;

    public Long bookCopyId;

    @NotNull(message = "User id is required")
    public Long userId;

}