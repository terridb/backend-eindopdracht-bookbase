package com.terrideboer.bookbase.services;

import com.terrideboer.bookbase.dtos.loans.LoanDto;
import com.terrideboer.bookbase.dtos.loans.LoanInputDto;
import com.terrideboer.bookbase.dtos.loans.LoanPatchDto;
import com.terrideboer.bookbase.exceptions.InvalidInputException;
import com.terrideboer.bookbase.exceptions.RecordNotFoundException;
import com.terrideboer.bookbase.mappers.LoanMapper;
import com.terrideboer.bookbase.models.BookCopy;
import com.terrideboer.bookbase.models.Loan;
import com.terrideboer.bookbase.models.User;
import com.terrideboer.bookbase.repositories.BookCopyRepository;
import com.terrideboer.bookbase.repositories.LoanRepository;
import com.terrideboer.bookbase.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookCopyRepository bookCopyRepository;
    private final UserRepository userRepository;

    public LoanService(LoanRepository loanRepository, BookCopyRepository bookCopyRepository, UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.userRepository = userRepository;
    }

    public List<LoanDto> getAllLoans() {
        List<Loan> loans = loanRepository.findAll();
        List<LoanDto> dtoLoans = new ArrayList<>();

        for (Loan loan : loans) {
            dtoLoans.add(LoanMapper.toDto(loan));
        }

        return dtoLoans;
    }

    public LoanDto getLoanById(Long id) {
        return LoanMapper.toDto(
                loanRepository.findById(id)
                        .orElseThrow(() -> new RecordNotFoundException("Loan with id " + id + " not found")));
    }

    public List<LoanDto> getLoansByUserId(Long id) {
        User user =   userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("User with id " + id + " not found"));

        List<Loan> loans = loanRepository.findByUser(user);
        List<LoanDto> dtoLoans = new ArrayList<>();

        for (Loan loan : loans) {
            dtoLoans.add(LoanMapper.toDto(loan));
        }

        return dtoLoans;
    }

    public LoanDto postLoan(LoanInputDto loanInputDto) {
        Loan loan = LoanMapper.toEntity(loanInputDto, null);
        BookCopy bookCopy = bookCopyRepository.findById(loanInputDto.bookCopyId)
                .orElseThrow(() -> new RecordNotFoundException(("Book-copy with id " + loanInputDto.bookCopyId + " not found")));
        User user = userRepository.findById(loanInputDto.userId)
                .orElseThrow(() -> new RecordNotFoundException(("User with id " + loanInputDto.bookCopyId + " not found")));

        if (loanInputDto.loanDate == null) {
            loan.setLoanDate(LocalDate.now());
        }

        loan.setBookCopy(bookCopy);
        loan.setUser(user);
//        todo check tracking nr
//        todo relaties user en bookcopy bij verwijderen

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
        loanRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Loan with id " + id + " not found"));
        loanRepository.deleteById(id);
    }

    public LoanDto patchLoan(Long id, LoanPatchDto loanPatchDto) {
        Loan existingLoan = loanRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Loan with id " + id + " not found"));

        if (loanPatchDto.loanPeriodInDays != null) {
            if (loanPatchDto.loanPeriodInDays <= 0) {
                throw new InvalidInputException("Loan period in days must be a positive number");
            }
            existingLoan.setLoanPeriodInDays(loanPatchDto.loanPeriodInDays);
        }

//        todo validatie enum
        if (loanPatchDto.loanStatus != null) {
            existingLoan.setLoanStatus(loanPatchDto.loanStatus);
        }

        if (loanPatchDto.loanDate != null) {
            if (loanPatchDto.loanDate.isAfter(LocalDate.now())) {
                throw new InvalidInputException("Loan date cannot be in the future");
            }
            existingLoan.setLoanPeriodInDays(loanPatchDto.loanPeriodInDays);
        }

        if (loanPatchDto.bookCopyId != null) {
            BookCopy bookCopy = bookCopyRepository.findById(loanPatchDto.bookCopyId)
                    .orElseThrow(() -> new RecordNotFoundException(("Book-copy with id " + loanPatchDto.bookCopyId + " not found")));

            existingLoan.setBookCopy(bookCopy);
        }

        if (loanPatchDto.userId != null) {
            User user = userRepository.findById(loanPatchDto.userId)
                    .orElseThrow(() -> new RecordNotFoundException(("User with id " + loanPatchDto.userId + " not found")));

            existingLoan.setUser(user);
        }

        Loan savedLoan = loanRepository.save(existingLoan);
        return LoanMapper.toDto(savedLoan);
//        todo overal trim toevoegen
    }

}
