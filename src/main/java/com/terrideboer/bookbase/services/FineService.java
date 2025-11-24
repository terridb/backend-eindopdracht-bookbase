package com.terrideboer.bookbase.services;

import com.terrideboer.bookbase.dtos.fines.FineDto;
import com.terrideboer.bookbase.dtos.fines.FineInputDto;
import com.terrideboer.bookbase.exceptions.AlreadyExistsException;
import com.terrideboer.bookbase.exceptions.ForbiddenException;
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

        Fine updatedFine = FineMapper.toEntity(fineInputDto, existingFine);

        Fine savedFine = fineRepository.save(updatedFine);
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
