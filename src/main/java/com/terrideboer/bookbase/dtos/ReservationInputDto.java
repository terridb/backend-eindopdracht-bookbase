package com.terrideboer.bookbase.dtos;

import com.terrideboer.bookbase.models.enums.ReservationStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class ReservationInputDto {

    @NotNull(message = "Reservation date is required")
    public LocalDate reservationDate;

    public ReservationStatus reservationStatus;

    @NotNull(message = "User id is required")
    public Long userId;

    @NotNull(message = "Book copy id is required")
    public Long bookCopyId;
}
