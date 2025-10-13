package com.terrideboer.bookbase.repositories;

import com.terrideboer.bookbase.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
