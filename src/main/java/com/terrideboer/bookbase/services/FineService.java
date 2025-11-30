package com.terrideboer.bookbase.services;

import com.terrideboer.bookbase.dtos.fines.FineDto;
import com.terrideboer.bookbase.dtos.fines.FineInputDto;
import com.terrideboer.bookbase.exceptions.AlreadyExistsException;
import com.terrideboer.bookbase.exceptions.ForbiddenException;
import com.terrideboer.bookbase.exceptions.InvalidInputException;
import com.terrideboer.bookbase.exceptions.RecordNotFoundException;
import com.terrideboer.bookbase.mappers.FineMapper;
import com.terrideboer.bookbase.models.Fine;
import com.terrideboer.bookbase.models.Loan;
import com.terrideboer.bookbase.models.User;
import com.terrideboer.bookbase.models.enums.PaymentStatus;
import com.terrideboer.bookbase.repositories.FineRepository;
import com.terrideboer.bookbase.repositories.LoanRepository;
import com.terrideboer.bookbase.utils.LoanUtils;
import com.terrideboer.bookbase.utils.UserUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class FineService {

    private final FineRepository fineRepository;
    private final LoanRepository loanRepository;

    public FineService(FineRepository fineRepository, LoanRepository loanRepository) {
        this.fineRepository = fineRepository;
        this.loanRepository = loanRepository;
    }

    public List<FineDto> getAllFines(String status) {
        List<FineDto> dtoFines = new ArrayList<>();
        List<Fine> fines;

        if (status == null || status.isBlank()) {
            fines = fineRepository.findAll(Sort.by("id").ascending());
        } else {
            PaymentStatus enumStatus = PaymentStatus.valueOf(status.trim().toUpperCase());
            fines = fineRepository.findByPaymentStatus(enumStatus);
        }

        for (Fine fine : fines) {
            dtoFines.add(FineMapper.toDto(fine));
        }

        return dtoFines;
    }

    public FineDto getFineById(Long id) {
        Fine fine = fineRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Fine with id " + id + " not found"));

        User user = fine.getLoan().getUser();
        if (!UserUtils.isOwnerOrAdmin(user)) {
            throw new ForbiddenException();
        }

        return FineMapper.toDto(fine);
    }

    public FineDto postManualFine(FineInputDto fineInputDto, Long loanId) {
        Loan existingLoan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RecordNotFoundException(("Loan with id " + loanId + " not found")));

        if (existingLoan.getFine() != null) {
            throw new AlreadyExistsException("This loan already has an existing fine. Manual fine could not be added");
        }

        if (fineInputDto.fineAmount == null) {
            throw new InvalidInputException("FineAmount is required");
        }

        if ((fineInputDto.paymentStatus != null
                && fineInputDto.paymentStatus.equals(PaymentStatus.PAID))
                && fineInputDto.paymentDate == null) {
            throw new InvalidInputException("PaymentDate is required when creating a fine with status PAID");
        }

        if ((fineInputDto.paymentStatus == null
                || fineInputDto.paymentStatus.equals(PaymentStatus.NOT_PAID))
                && fineInputDto.paymentDate != null) {
            throw new InvalidInputException("PaymentStatus cannot be NOT_PAID when a paymentDate is provided");
        }

        Fine fine = FineMapper.toEntity(fineInputDto, null);
        fine.setLoan(existingLoan);

        if (fine.getPaymentStatus() == null) {
            fine.setPaymentStatus(PaymentStatus.NOT_PAID);
        }

        Fine savedFine = fineRepository.save(fine);
        return FineMapper.toDto(savedFine);
    }

    public FineDto updateFine(Long id, FineInputDto fineInputDto) {
        Fine existingFine = fineRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(("Fine with id " + id + " not found")));

        PaymentStatus newStatus = fineInputDto.paymentStatus != null
                ? fineInputDto.paymentStatus
                : existingFine.getPaymentStatus();

        LocalDate newPaymentDate = fineInputDto.paymentDate != null
                ? fineInputDto.paymentDate
                : existingFine.getPaymentDate();

        if (newStatus == PaymentStatus.PAID && newPaymentDate == null) {
            throw new InvalidInputException("PaymentDate is required when setting status to PAID");
        }

        if (newPaymentDate != null && newStatus == PaymentStatus.NOT_PAID) {
            throw new InvalidInputException("PaymentStatus cannot be NOT_PAID when a paymentDate is provided");
        }

        existingFine.setPaymentStatus(newStatus);
        existingFine.setPaymentDate(newPaymentDate);

        if (fineInputDto.fineAmount != null) {
            existingFine.setFineAmount(fineInputDto.fineAmount);
        }

        Fine savedFine = fineRepository.save(existingFine);
        return FineMapper.toDto(savedFine);
    }

    public void deleteFine(Long id) {
        Fine fine = fineRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(("Fine with id " + id + " not found")));

        Loan loan = fine.getLoan();
        loan.setFine(null);

        fineRepository.deleteById(id);
    }

    public FineDto payFine(Long id) {
        Fine fine = fineRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(("Fine with id " + id + " not found")));

        User user = fine.getLoan().getUser();
        if (!UserUtils.isOwnerOrAdmin(user)) {
            throw new ForbiddenException();
        }

        if (fine.getPaymentStatus() == PaymentStatus.PAID) {
            throw new AlreadyExistsException("This fine has already been paid");
        }

        fine.setPaymentStatus(PaymentStatus.PAID);
        fine.setPaymentDate(LocalDate.now());
        Fine savedFine = fineRepository.save(fine);

        return FineMapper.toDto(savedFine);
    }

    public Fine generateFine(Loan loan) {
        long daysLate = LoanUtils.getDaysLateFromLoan(loan);

        double fineAmount = daysLate * 0.5;

        if (fineAmount > 10) {
            fineAmount = 10;
        }

        Fine fine = new Fine();
        fine.setLoan(loan);
        fine.setFineAmount(BigDecimal.valueOf(fineAmount));
        fine.setPaymentStatus(PaymentStatus.NOT_PAID);

        return fineRepository.save(fine);
    }
}
