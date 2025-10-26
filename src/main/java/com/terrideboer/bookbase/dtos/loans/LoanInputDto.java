package com.terrideboer.bookbase.dtos.loans;

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

    public Long bookCopyId;
//    todo logica in service om te checken welke van de twee is ingevuld
    public String trackingNumber;

    @NotNull(message = "User id is required")
    public Long userId;
}