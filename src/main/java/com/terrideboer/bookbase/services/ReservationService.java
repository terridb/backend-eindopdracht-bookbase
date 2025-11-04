package com.terrideboer.bookbase.services;

import com.terrideboer.bookbase.dtos.loans.LoanDto;
import com.terrideboer.bookbase.dtos.loans.LoanInputDto;
import com.terrideboer.bookbase.dtos.reservations.ReservationDto;
import com.terrideboer.bookbase.dtos.reservations.ReservationInputDto;
import com.terrideboer.bookbase.dtos.reservations.ReservationPatchDto;
import com.terrideboer.bookbase.exceptions.InvalidInputException;
import com.terrideboer.bookbase.exceptions.RecordNotFoundException;
import com.terrideboer.bookbase.mappers.ReservationMapper;
import com.terrideboer.bookbase.models.*;
import com.terrideboer.bookbase.models.enums.ReservationStatus;
import com.terrideboer.bookbase.repositories.BookCopyRepository;
import com.terrideboer.bookbase.repositories.LoanRepository;
import com.terrideboer.bookbase.repositories.ReservationRepository;
import com.terrideboer.bookbase.repositories.UserRepository;
import com.terrideboer.bookbase.utils.DateUtils;
import org.openpdf.text.*;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookCopyRepository bookCopyRepository;
    private final UserRepository userRepository;
    private final LoanService loanService;
    private final LoanRepository loanRepository;

    public ReservationService(ReservationRepository reservationRepository, BookCopyRepository bookCopyRepository, UserRepository userRepository, LoanService loanService, LoanRepository loanRepository) {
        this.reservationRepository = reservationRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.userRepository = userRepository;
        this.loanService = loanService;
        this.loanRepository = loanRepository;
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

    public List<ReservationDto> getAllToBePreparedReservations() {
        List<Reservation> reservations = reservationRepository.findReservationsByReservationStatus(ReservationStatus.PENDING,
                Sort.by(Sort.Direction.ASC, "reservationDate"));
        List<ReservationDto> dtoReservations = new ArrayList<>();

        for (Reservation reservation : reservations) {
            dtoReservations.add(ReservationMapper.toDto(reservation));
        }

        return dtoReservations;
    }

    public byte[] generateReservationsPdf() {
        List<ReservationDto> reservations = reservationRepository.findReservationsByReservationStatus(ReservationStatus.PENDING,
                Sort.by("reservationDate"))
                .stream()
                .map(ReservationMapper::toDto)
                .toList();

        try {
            Document document = new Document(PageSize.A4.rotate());
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, out);

            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            fontTitle.setSize(18);

            HeaderFooter footer = new HeaderFooter(new Phrase("Page "), true);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setBorderWidthBottom(0);
            document.setFooter(footer);

            document.open();

            Paragraph title = new Paragraph("Reservations to prepare on " + DateUtils.formatDate(LocalDate.now()), fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.addCell("Reservation ID");
            table.addCell("Book title");
            table.addCell("Authors");
            table.addCell("Tracking nr.");
            table.addCell("Member");
            table.addCell("Reservation Date");

            for (ReservationDto reservation : reservations) {
                table.addCell(String.valueOf(reservation.id));
                table.addCell(reservation.bookCopy.book.title);
                table.addCell(reservation.bookCopy.book.authors
                        .stream()
                        .map(Author -> Author.displayName)
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("-"));
                table.addCell(reservation.bookCopy.trackingNumber);
                table.addCell(reservation.user.firstName + " " + reservation.user.lastName);
                table.addCell(DateUtils.formatDate(reservation.reservationDate));
            }

            document.add(table);
            document.close();

            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
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

        LoanDto createdLoan = loanService.postLoan(loanInput);

        reservation.setReservationStatus(ReservationStatus.COLLECTED);
        reservation.setCollectedDate(LocalDate.now());

        Loan databaseLoan = loanRepository.findById(createdLoan.id)
                .orElseThrow(() -> new RecordNotFoundException("Loan not found after creation"));
        reservation.setLoan(databaseLoan);

        reservationRepository.save(reservation);
        return ReservationMapper.toDto(reservation);
    }

    public ReservationDto cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Reservation with id " + id + " not found"));

        if (reservation.getReservationStatus() == ReservationStatus.COLLECTED) {
            throw new InvalidInputException("Collected reservations cannot be cancelled");
        }

        reservation.setReservationStatus(ReservationStatus.CANCELLED);

        reservationRepository.save(reservation);
        return ReservationMapper.toDto(reservation);
    }

//    todo als tijd over: expired reservations
    //    todo indien tijd over: relatie met book zodat iemand een book reserveert ipv copy. Backend kijkt vervolgens welke bookcopy available is.
}
