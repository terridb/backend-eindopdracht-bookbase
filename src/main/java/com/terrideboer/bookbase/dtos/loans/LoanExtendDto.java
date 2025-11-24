package com.terrideboer.bookbase.dtos.loans;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class LoanExtendDto {

    @NotNull
    @Positive(message = "Extra days must be a positive number")
    public Integer extraDays;
}
