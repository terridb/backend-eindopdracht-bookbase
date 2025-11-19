package com.terrideboer.bookbase.services;

import com.terrideboer.bookbase.dtos.loans.LoanDto;
import com.terrideboer.bookbase.dtos.loans.LoanInputDto;
import com.terrideboer.bookbase.dtos.loans.LoanWithFineDto;
import com.terrideboer.bookbase.exceptions.ForbiddenException;
import com.terrideboer.bookbase.exceptions.RecordNotFoundException;
import com.terrideboer.bookbase.mappers.LoanMapper;
import com.terrideboer.bookbase.models.*;
import com.terrideboer.bookbase.models.enums.LoanStatus;
import com.terrideboer.bookbase.repositories.BookCopyRepository;
import com.terrideboer.bookbase.repositories.LoanRepository;
import com.terrideboer.bookbase.repositories.UserRepository;
import com.terrideboer.bookbase.utils.LoanUtils;
import com.terrideboer.bookbase.utils.UserUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookCopyRepository bookCopyRepository;
    private final UserRepository userRepository;
    private final FineService fineService;

    public LoanService(LoanRepository loanRepository,
                       BookCopyRepository bookCopyRepository,
                       UserRepository userRepository,
                       FineService fineService) {
        this.loanRepository = loanRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.userRepository = userRepository;
        this.fineService = fineService;
    }

    public List<LoanDto> getAllLoans() {
        List<Loan> loans = loanRepository.findAll(Sort.by("id").ascending());
        List<LoanDto> dtoLoans = new ArrayList<>();

        for (Loan loan : loans) {
            dtoLoans.add(LoanMapper.toDto(loan));
        }

        return dtoLoans;
    }

    public LoanWithFineDto getLoanById(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Loan with id " + id + " not found"));

        User user = loan.getUser();
        if (!UserUtils.isOwnerOrAdmin(user)) {
            throw new ForbiddenException();
        }

        return LoanMapper.toLoanWithFineDto(loan);
    }

    public LoanDto postLoan(LoanInputDto loanInputDto) {
        Loan loan = buildLoanEntity(loanInputDto);
        Loan savedLoan = loanRepository.save(loan);
        return LoanMapper.toDto(savedLoan);
    }

    public LoanDto putLoan(Long id, LoanInputDto loanInputDto) {
        Loan existingLoan = loanRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Loan with id " + id + " not found"));

        Loan updatedLoan = LoanMapper.toEntity(loanInputDto, existingLoan);
        Loan savedLoan = loanRepository.save(updatedLoan);
        return LoanMapper.toDto(savedLoan);
    }

    public void deleteLoan(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Loan with id " + id + " not found"));

        if (loan.getReservation() != null) {
            Reservation reservation = loan.getReservation();
            loan.setReservation(null);
            reservation.setLoan(null);
        }

        loanRepository.delete(loan);
    }

    public LoanWithFineDto returnBook(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Loan with id " + id + " not found"));

        loan.setReturnDate(LocalDate.now());
        loan.setLoanStatus(LoanStatus.RETURNED);

        if (LoanUtils.checkIfLoanIsExpired(loan)) {
            Fine fine = fineService.generateFine(loan);
            loan.setFine(fine);
        }

        loanRepository.save(loan);
        return LoanMapper.toLoanWithFineDto(loan);
    }

    public Loan createLoanEntity(LoanInputDto loanInputDto) {
        Loan loan = buildLoanEntity(loanInputDto);
        return loanRepository.save(loan);
    }

    private Loan buildLoanEntity(LoanInputDto loanInputDto) {
        Loan loan = LoanMapper.toEntity(loanInputDto, null);

        BookCopy bookCopy = bookCopyRepository.findById(loanInputDto.bookCopyId)
                .orElseThrow(() -> new RecordNotFoundException(("Book-copy with id " + loanInputDto.bookCopyId + " not found")));
        User user = userRepository.findById(loanInputDto.userId)
                .orElseThrow(() -> new RecordNotFoundException(("User with id " + loanInputDto.userId + " not found")));

        if (loanInputDto.loanDate == null) {
            loan.setLoanDate(LocalDate.now());
        }

        loan.setBookCopy(bookCopy);
        loan.setUser(user);

        return loan;
    }
}
