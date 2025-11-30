package com.terrideboer.bookbase.dtos.fines;

import com.terrideboer.bookbase.models.enums.PaymentStatus;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FineInputDto {

    @Positive(message = "Fine amount must be a positive number")
    public BigDecimal fineAmount;

    public PaymentStatus paymentStatus;

    @PastOrPresent
    public LocalDate paymentDate;
}

