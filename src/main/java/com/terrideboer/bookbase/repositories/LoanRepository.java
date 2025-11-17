package com.terrideboer.bookbase.repositories;

import com.terrideboer.bookbase.models.Loan;
import com.terrideboer.bookbase.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}
