package com.terrideboer.bookbase.dtos;

import com.terrideboer.bookbase.models.enums.ReservationStatus;

import java.time.LocalDate;

public class ReservationDto {
    public Long id;
    public LocalDate reservationDate;
    public ReservationStatus reservationStatus;
    //    todo mappers userid en bookcopyid
    public Long userId;
    public Long bookCopyId;
}
