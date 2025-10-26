package com.terrideboer.bookbase.services;

import com.terrideboer.bookbase.dtos.loans.LoanDto;
import com.terrideboer.bookbase.dtos.loans.LoanInputDto;
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

    //    todo toevoegen dat alle loans van 1 user opgevraagd kunnen worden
    public LoanDto getLoanById(Long id) {
        return LoanMapper.toDto(
                loanRepository.findById(id)
                        .orElseThrow(() -> new RecordNotFoundException("Loan with id " + id + " not found")));
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
}
