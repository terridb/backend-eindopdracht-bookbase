package com.terrideboer.bookbase.utils;

import com.terrideboer.bookbase.models.Loan;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LoanUtils {
    public static long getDaysLateFromLoan(Loan loan) {
        if (loan.getReturnDate() != null) {
            return ChronoUnit.DAYS.between(loan.getLoanDate().plusDays(loan.getLoanPeriodInDays()),
                    loan.getReturnDate());
        } else {
            return ChronoUnit.DAYS.between(loan.getLoanDate().plusDays(loan.getLoanPeriodInDays()),
                    LocalDate.now());
        }

    }

    public static boolean checkIfLoanIsExpired(Loan loan) {
        long daysLate = getDaysLateFromLoan(loan);

        return daysLate > 0;
    }
}
