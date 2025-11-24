package com.terrideboer.bookbase.repositories;

import com.terrideboer.bookbase.models.Fine;
import com.terrideboer.bookbase.models.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FineRepository extends JpaRepository<Fine, Long> {
    List<Fine> findByPaymentStatus(PaymentStatus paymentStatus);
}
