package com.terrideboer.bookbase.dtos;

import com.terrideboer.bookbase.models.enums.LoanStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class LoanInputDto {

    @NotNull(message = "Loan date is required")
    public LocalDate loanDate;

    @Positive(message = "Loan period in days must be a positive number")
    public Integer loanPeriodInDays;

    public LoanStatus loanStatus;

    @NotNull(message = "Book copy id is required")
    public Long bookCopyId;

    @NotNull(message = "User id is required")
    public Long userId;
}