package com.terrideboer.bookbase.dtos.loans;

import com.terrideboer.bookbase.dtos.bookcopies.BookCopyDto;
import com.terrideboer.bookbase.dtos.users.UserSummaryDto;
import com.terrideboer.bookbase.models.enums.LoanStatus;

import java.time.LocalDate;

public class LoanDto {
    public Long id;
    public LocalDate loanDate;
    public LocalDate returnDate;
    public Integer loanPeriodInDays;
    public LoanStatus loanStatus;
    public BookCopyDto bookCopy;
    public UserSummaryDto user;
}
