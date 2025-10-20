package com.terrideboer.bookbase.dtos.fines;

import com.terrideboer.bookbase.models.enums.PaymentStatus;

import java.math.BigDecimal;

public class FineDto {
    public Long id;
    public BigDecimal fineAmount;
    public PaymentStatus paymentStatus;
    //    todo mapper loanid
    public Long loanId;
}
