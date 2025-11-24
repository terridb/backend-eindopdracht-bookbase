package com.terrideboer.bookbase.mappers;

import com.terrideboer.bookbase.dtos.loans.LoanDto;
import com.terrideboer.bookbase.dtos.loans.LoanInputDto;
import com.terrideboer.bookbase.dtos.loans.LoanSummaryDto;
import com.terrideboer.bookbase.dtos.loans.LoanWithFineDto;
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
        loanDto.returnDate = loan.getReturnDate();
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

    public static LoanSummaryDto toSummaryDto(Loan loan) {
        LoanSummaryDto loanSummaryDto = new LoanSummaryDto();

        loanSummaryDto.id = loan.getId();
        loanSummaryDto.loanDate = loan.getLoanDate();
        loanSummaryDto.returnDate = loan.getReturnDate();
        loanSummaryDto.loanPeriodInDays = loan.getLoanPeriodInDays();
        loanSummaryDto.loanStatus = loan.getLoanStatus();

        return loanSummaryDto;
    }

    public static LoanWithFineDto toLoanWithFineDto(Loan loan) {
        LoanWithFineDto loanWithFineDto = new LoanWithFineDto();

        loanWithFineDto.id = loan.getId();
        loanWithFineDto.loanDate = loan.getLoanDate();
        loanWithFineDto.returnDate = loan.getReturnDate();
        loanWithFineDto.loanPeriodInDays = loan.getLoanPeriodInDays();
        loanWithFineDto.loanStatus = loan.getLoanStatus();

        if (loan.getUser() != null) {
            loanWithFineDto.user = UserMapper.toSummaryDto(loan.getUser());
        }

        if (loan.getBookCopy() != null) {
            loanWithFineDto.bookCopy = BookCopyMapper.toDto(loan.getBookCopy());
        }

        if (loan.getFine() != null) {
            loanWithFineDto.fine = FineMapper.toSummaryDto(loan.getFine());
        }

        return loanWithFineDto;
    }
}
