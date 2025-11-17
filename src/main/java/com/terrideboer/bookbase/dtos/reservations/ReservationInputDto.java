package com.terrideboer.bookbase.dtos.reservations;

import jakarta.validation.constraints.NotNull;

public class ReservationInputDto {

    @NotNull(message = "User id is required")
    public Long userId;

    @NotNull(message = "Book copy id is required")
    public Long bookCopyId;
}
