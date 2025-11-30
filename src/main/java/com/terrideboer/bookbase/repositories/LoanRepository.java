package com.terrideboer.bookbase.repositories;

import com.terrideboer.bookbase.models.Loan;
import com.terrideboer.bookbase.models.enums.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByLoanStatus(LoanStatus loanStatus);
}
