package com.terrideboer.bookbase.services;

import com.terrideboer.bookbase.dtos.loans.LoanInputDto;
import com.terrideboer.bookbase.dtos.reservations.ReservationDto;
import com.terrideboer.bookbase.dtos.reservations.ReservationInputDto;
import com.terrideboer.bookbase.exceptions.ForbiddenException;
import com.terrideboer.bookbase.exceptions.InvalidInputException;
import com.terrideboer.bookbase.exceptions.RecordNotFoundException;
import com.terrideboer.bookbase.mappers.ReservationMapper;
import com.terrideboer.bookbase.models.*;
import com.terrideboer.bookbase.models.enums.ReservationStatus;
import com.terrideboer.bookbase.repositories.BookCopyRepository;
import com.terrideboer.bookbase.repositories.ReservationRepository;
import com.terrideboer.bookbase.repositories.UserRepository;
import com.terrideboer.bookbase.utils.UserUtils;
import org.openpdf.text.*;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookCopyRepository bookCopyRepository;
    private final UserRepository userRepository;
    private final LoanService loanService;
    private final ReservationPdfService reservationPdfService;

    public ReservationService(ReservationRepository reservationRepository,
                              BookCopyRepository bookCopyRepository,
                              UserRepository userRepository,
                              LoanService loanService, ReservationPdfService reservationPdfService) {
        this.reservationRepository = reservationRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.userRepository = userRepository;
        this.loanService = loanService;
        this.reservationPdfService = reservationPdfService;
    }

    public List<ReservationDto> getAllReservations(String status) {
        List<ReservationDto> dtoReservations = new ArrayList<>();
        List<Reservation> reservations;

        if (status == null || status.isBlank()) {
            reservations = reservationRepository.findAll(Sort.by("id").ascending());
        } else {
            ReservationStatus enumStatus = ReservationStatus.valueOf(status.trim().toUpperCase());
            reservations = reservationRepository.findByReservationStatus(enumStatus);
        }

        for (Reservation reservation : reservations) {
            dtoReservations.add(ReservationMapper.toDto(reservation));
        }

        return dtoReservations;
    }

    public ReservationDto getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Reservation with id " + id + " not found"));

        User user = reservation.getUser();
        if (!UserUtils.isOwnerOrAdmin(user)) {
            throw new ForbiddenException();
        }

        return ReservationMapper.toDto(reservation);
    }

    public ReservationDto postReservation(ReservationInputDto reservationInputDto) {
        BookCopy bookCopy = bookCopyRepository.findById(reservationInputDto.bookCopyId)
                .orElseThrow(() -> new RecordNotFoundException(("Book-copy with id " + reservationInputDto.bookCopyId + " not found")));
        User user = userRepository.findById(reservationInputDto.userId)
                .orElseThrow(() -> new RecordNotFoundException(("User with id " + reservationInputDto.userId + " not found")));

        if (!UserUtils.isOwnerOrAdmin(user)) {
            throw new ForbiddenException("You're not allowed to create reservations for other members");
        }

        Reservation reservation = new Reservation();

        reservation.setBookCopy(bookCopy);
        reservation.setUser(user);

        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationMapper.toDto(savedReservation);
    }

    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Reservation with id " + id + " not found"));

        if (reservation.getLoan() != null) {
            Loan loan = reservation.getLoan();
            loan.setReservation(null);
            reservation.setLoan(null);
        }

        reservationRepository.delete(reservation);
    }

    public ReservationDto markReservationReadyForPickup(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Reservation with id " + id + " not found"));

        if (reservation.getReservationStatus() != ReservationStatus.PENDING) {
            throw new InvalidInputException("Only pending reservations can be marked as ready for pickup");
        }

        reservation.setReservationStatus(ReservationStatus.READY_FOR_PICKUP);
        reservation.setReadyForPickupDate(LocalDate.now());

        reservationRepository.save(reservation);
        return ReservationMapper.toDto(reservation);
    }

    public ReservationDto markReservationAsCollected(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Reservation with id " + id + " not found"));

        if (reservation.getReservationStatus() != ReservationStatus.READY_FOR_PICKUP) {
            throw new InvalidInputException("Only reservations that are ready for pickup can be marked as collected");
        }

        LoanInputDto loanInput = new LoanInputDto();
        loanInput.userId = reservation.getUser().getId();
        loanInput.bookCopyId = reservation.getBookCopy().getId();
        reservation.setLoan(loanService.createLoanEntity(loanInput));

        reservation.setReservationStatus(ReservationStatus.COLLECTED);
        reservation.setCollectedDate(LocalDate.now());

        reservationRepository.save(reservation);
        return ReservationMapper.toDto(reservation);
    }

    public ReservationDto cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Reservation with id " + id + " not found"));
        User user = reservation.getUser();

        if (!UserUtils.isOwnerOrAdmin(user)) {
            throw new ForbiddenException("You're not allowed to cancel reservations for other members");
        }

        if (reservation.getReservationStatus() == ReservationStatus.COLLECTED) {
            throw new InvalidInputException("Collected reservations cannot be cancelled");
        }

        reservation.setReservationStatus(ReservationStatus.CANCELLED);

        reservationRepository.save(reservation);
        return ReservationMapper.toDto(reservation);
    }

    public byte[] generateReservationsPdf() {
        List<ReservationDto> reservations = reservationRepository
                .findByReservationStatus(ReservationStatus.PENDING)
                .stream()
                .map(ReservationMapper::toDto)
                .toList();

        return reservationPdfService.generateReservationsPdf(reservations);
    }
}
