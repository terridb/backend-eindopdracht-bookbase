package com.terrideboer.bookbase.services;

import com.terrideboer.bookbase.dtos.fines.FineDto;
import com.terrideboer.bookbase.dtos.fines.FineInputDto;
import com.terrideboer.bookbase.exceptions.RecordNotFoundException;
import com.terrideboer.bookbase.mappers.FineMapper;
import com.terrideboer.bookbase.models.Fine;
import com.terrideboer.bookbase.models.Loan;
import com.terrideboer.bookbase.models.enums.PaymentStatus;
import com.terrideboer.bookbase.repositories.FineRepository;
import com.terrideboer.bookbase.repositories.LoanRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class FineService {

    private final FineRepository fineRepository;

    public FineService(FineRepository fineRepository) {
        this.fineRepository = fineRepository;
    }

    public List<FineDto> getAllFines() {
        List<Fine> fines = fineRepository.findAll();
        List<FineDto> dtoFines = new ArrayList<>();

        for (Fine fine : fines) {
            dtoFines.add(FineMapper.toDto(fine));
        }

        return dtoFines;
    }

    public FineDto getFineById(Long id) {
        return FineMapper.toDto(
                fineRepository.findById(id)
                        .orElseThrow(() -> new RecordNotFoundException("Fine with id " + id + " not found")));
    }

//    public FineDto postManualFine(FineInputDto fineInputDto, Long loanId) {
//        Fine fine = FineMapper.toEntity(fineInputDto, null);
//        Loan existingLoan = loanRepository.findById(loanId)
//                .orElseThrow(() -> new RecordNotFoundException(("Loan with id " + loanId + " not found")));
//
//        fine.setLoan(existingLoan);
//
//
//    }

    public Fine generateFine(Loan loan) {
        long daysLate = ChronoUnit.DAYS.between(loan.getLoanDate().plusDays(loan.getLoanPeriodInDays()),
                loan.getReturnDate());

        double fineAmount = daysLate * 0.5;

        if (fineAmount > 10) {
            fineAmount = 10;
        }

        Fine fine = new Fine();
        fine.setLoan(loan);
        fine.setFineAmount(BigDecimal.valueOf(fineAmount));
        fine.setPaymentStatus(PaymentStatus.NOT_PAID);

        fineRepository.save(fine);
        return fine;
    }
}
