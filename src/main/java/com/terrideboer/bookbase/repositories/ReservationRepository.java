package com.terrideboer.bookbase.repositories;

import com.terrideboer.bookbase.models.Reservation;
import com.terrideboer.bookbase.models.enums.ReservationStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findReservationsByReservationStatus(ReservationStatus reservationStatus, Sort sort);
}
