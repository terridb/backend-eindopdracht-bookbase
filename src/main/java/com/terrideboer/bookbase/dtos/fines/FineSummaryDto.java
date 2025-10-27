package com.terrideboer.bookbase.dtos.fines;

import com.terrideboer.bookbase.models.enums.PaymentStatus;

import java.math.BigDecimal;

public class FineSummaryDto {
    public Long id;
    public BigDecimal fineAmount;
    public PaymentStatus paymentStatus;
}
