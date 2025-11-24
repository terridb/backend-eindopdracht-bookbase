package com.terrideboer.bookbase.mappers;

import com.terrideboer.bookbase.dtos.reservations.ReservationDto;
import com.terrideboer.bookbase.models.Reservation;

public class ReservationMapper {

    public static ReservationDto toDto(Reservation reservation) {
        ReservationDto reservationDto = new ReservationDto();

        reservationDto.id = reservation.getId();
        reservationDto.reservationDate = reservation.getReservationDate();
        reservationDto.readyForPickupDate = reservation.getReadyForPickupDate();
        reservationDto.collectedDate = reservation.getCollectedDate();
        reservationDto.reservationStatus = reservation.getReservationStatus();

        if (reservation.getBookCopy() != null) {
            reservationDto.bookCopy = BookCopyMapper.toDto(reservation.getBookCopy());
        }

        if (reservation.getUser() != null) {
            reservationDto.user = UserMapper.toSummaryDto(reservation.getUser());
        }

        if (reservation.getLoan() != null) {
            reservationDto.loan = LoanMapper.toSummaryDto(reservation.getLoan());
        }

        return reservationDto;
    }
}
