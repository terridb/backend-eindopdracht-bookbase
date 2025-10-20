package com.terrideboer.bookbase.dtos.fines;

import com.terrideboer.bookbase.models.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class FineInputDto {

    @NotNull(message = "Fine amount is required")
    @Positive(message = "Fine amount must be a positive number")
    public BigDecimal fineAmount;

    public PaymentStatus paymentStatus;

    @NotNull(message = "Loan id is required")
    public Long loanId;
}

