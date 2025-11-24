package com.terrideboer.bookbase.dtos.loans;

import jakarta.validation.constraints.Positive;

public class LoanExtendDto {

    @Positive(message = "Extra days must be a positive number")
    public int extraDays;
}
