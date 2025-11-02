package com.terrideboer.bookbase.dtos.reservations;

import com.terrideboer.bookbase.dtos.bookcopies.BookCopyDto;
import com.terrideboer.bookbase.dtos.users.UserSummaryDto;
import com.terrideboer.bookbase.models.enums.ReservationStatus;

import java.time.LocalDate;

public class ReservationDto {
    public Long id;
    public LocalDate reservationDate;
    public LocalDate readyForPickupDate;
    public LocalDate collectedDate;
    public ReservationStatus reservationStatus;
    public UserSummaryDto user;
    public BookCopyDto bookCopy;
//    todo losse dto's voor medewerkers vs leden
}
