package com.terrideboer.bookbase.mappers;

import com.terrideboer.bookbase.dtos.loans.LoanDto;
import com.terrideboer.bookbase.dtos.loans.LoanInputDto;
import com.terrideboer.bookbase.models.Loan;

public class LoanMapper {

    public static Loan toEntity(LoanInputDto loanInputDto, Loan loan) {
        if (loan == null) {
            loan = new Loan();
        }

        loan.setLoanDate(loanInputDto.loanDate);

        if (loanInputDto.loanPeriodInDays != null) {
            loan.setLoanPeriodInDays(loanInputDto.loanPeriodInDays);
        }

        if (loanInputDto.loanStatus != null) {
            loan.setLoanStatus(loanInputDto.loanStatus);
        }

        return loan;
    }

    public static LoanDto toDto(Loan loan) {
        LoanDto loanDto = new LoanDto();

        loanDto.id = loan.getId();
        loanDto.loanDate = loan.getLoanDate();
        loanDto.loanPeriodInDays = loan.getLoanPeriodInDays();
        loanDto.loanStatus = loan.getLoanStatus();

        if (loan.getBookCopy() != null) {
            loanDto.bookCopy = BookCopyMapper.toDto(loan.getBookCopy());
        }

        if (loan.getUser() != null) {
            loanDto.user = UserMapper.toSummaryDto(loan.getUser());
        }

        return loanDto;
    }
}
