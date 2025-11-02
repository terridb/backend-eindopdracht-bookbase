package com.terrideboer.bookbase.services;

import com.terrideboer.bookbase.dtos.loans.LoanWithFineDto;
import com.terrideboer.bookbase.dtos.reservations.ReservationDto;
import com.terrideboer.bookbase.dtos.reservations.ReservationInputDto;
import com.terrideboer.bookbase.dtos.reservations.ReservationPatchDto;
import com.terrideboer.bookbase.exceptions.RecordNotFoundException;
import com.terrideboer.bookbase.mappers.LoanMapper;
import com.terrideboer.bookbase.mappers.ReservationMapper;
import com.terrideboer.bookbase.models.BookCopy;
import com.terrideboer.bookbase.models.Loan;
import com.terrideboer.bookbase.models.Reservation;
import com.terrideboer.bookbase.models.User;
import com.terrideboer.bookbase.models.enums.ReservationStatus;
import com.terrideboer.bookbase.repositories.BookCopyRepository;
import com.terrideboer.bookbase.repositories.ReservationRepository;
import com.terrideboer.bookbase.repositories.UserRepository;
import com.terrideboer.bookbase.utils.LoanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookCopyRepository bookCopyRepository;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository, BookCopyRepository bookCopyRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.userRepository = userRepository;
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

    public ReservationDto postReservation(ReservationInputDto reservationInputDto) {
        BookCopy bookCopy = bookCopyRepository.findById(reservationInputDto.bookCopyId)
                .orElseThrow(() -> new RecordNotFoundException(("Book-copy with id " + reservationInputDto.bookCopyId + " not found")));
        User user = userRepository.findById(reservationInputDto.userId)
                .orElseThrow(() -> new RecordNotFoundException(("User with id " + reservationInputDto.bookCopyId + " not found")));

        Reservation reservation = new Reservation();

        reservation.setBookCopy(bookCopy);
        reservation.setUser(user);

        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationMapper.toDto(savedReservation);
    }
//    todo indien tijd over: relatie met book zodat iemand een book reserveert ipv copy. Backend kijkt vervolgens welke bookcopy available is.

    public ReservationDto patchReservation(Long id, ReservationPatchDto reservationPatchDto) {
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Reservation with id " + id + " not found"));

        if (reservationPatchDto.readyForPickupDate != null) {
            existingReservation.setReadyForPickupDate(reservationPatchDto.readyForPickupDate);
        }

        if (reservationPatchDto.collectedDate != null) {
            existingReservation.setCollectedDate(reservationPatchDto.collectedDate);
        }

        if (reservationPatchDto.reservationStatus != null) {
            existingReservation.setReservationStatus(reservationPatchDto.reservationStatus);
        }

        Reservation savedReservation = reservationRepository.save(existingReservation);
        return ReservationMapper.toDto(savedReservation);
    }

}
