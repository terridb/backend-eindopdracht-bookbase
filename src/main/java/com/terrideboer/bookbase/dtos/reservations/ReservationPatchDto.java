package com.terrideboer.bookbase.dtos.reservations;

import com.terrideboer.bookbase.models.enums.ReservationStatus;

import java.time.LocalDate;

public class ReservationPatchDto {
    public LocalDate readyForPickupDate;
    public LocalDate collectedDate;
    public ReservationStatus reservationStatus;
}
