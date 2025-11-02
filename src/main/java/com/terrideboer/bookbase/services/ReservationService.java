package com.terrideboer.bookbase.services;

import com.terrideboer.bookbase.dtos.loans.LoanWithFineDto;
import com.terrideboer.bookbase.dtos.reservations.ReservationDto;
import com.terrideboer.bookbase.exceptions.RecordNotFoundException;
import com.terrideboer.bookbase.mappers.LoanMapper;
import com.terrideboer.bookbase.mappers.ReservationMapper;
import com.terrideboer.bookbase.models.Loan;
import com.terrideboer.bookbase.models.Reservation;
import com.terrideboer.bookbase.repositories.ReservationRepository;
import com.terrideboer.bookbase.utils.LoanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationDto> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        List<ReservationDto> dtoReservations = new ArrayList<>();

        for (Reservation reservation : reservations) {
            dtoReservations.add(ReservationMapper.toDto(reservation));
        }

        return dtoReservations;
    }

    public ReservationDto getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Reservation with id " + id + " not found"));


        return ReservationMapper.toDto(reservation);
    }
}
