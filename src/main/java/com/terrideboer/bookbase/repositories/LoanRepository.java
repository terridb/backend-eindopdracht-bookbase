package com.terrideboer.bookbase.repositories;

import com.terrideboer.bookbase.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}
