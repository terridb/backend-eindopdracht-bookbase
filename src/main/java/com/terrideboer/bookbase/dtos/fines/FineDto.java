package com.terrideboer.bookbase.dtos.fines;

import com.terrideboer.bookbase.models.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FineDto {
    public Long id;
    public BigDecimal fineAmount;
    public LocalDate paymentDate;
    public PaymentStatus paymentStatus;
    public Long loanId;
}
