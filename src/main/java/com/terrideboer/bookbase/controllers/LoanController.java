package com.terrideboer.bookbase.controllers;

import com.terrideboer.bookbase.dtos.loans.LoanDto;
import com.terrideboer.bookbase.dtos.loans.LoanInputDto;
import com.terrideboer.bookbase.dtos.loans.LoanPatchDto;
import com.terrideboer.bookbase.dtos.loans.LoanWithFineDto;
import com.terrideboer.bookbase.services.LoanService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    //    Endpoint to get all loans
    @GetMapping
    public ResponseEntity<List<LoanDto>> getAllLoans() {

        return ResponseEntity.ok(loanService.getAllLoans());
    }

    //    Endpoint to get a loan by id
    @GetMapping("/{id}")
    public ResponseEntity<LoanWithFineDto> getLoanById(@PathVariable Long id) {

        return ResponseEntity.ok(loanService.getLoanById(id));
    }

    //    Endpoint to create a new loan
    @PostMapping
    public ResponseEntity<LoanDto> postLoan(@Valid @RequestBody LoanInputDto loanInputDto) {
        LoanDto loanDto = loanService.postLoan(loanInputDto);

        URI uri = URI.create("/loans/" + loanDto.id);

        return ResponseEntity.created(uri).body(loanDto);
    }

    //    Endpoint to adjust a loan for a book
    @PutMapping("/{id}")
    public ResponseEntity<LoanDto> putLoan(@PathVariable Long id, @Valid @RequestBody LoanInputDto loanInputDto) {
        LoanDto loanDto = loanService.putLoan(id, loanInputDto);

        return ResponseEntity.ok(loanDto);
    }

    //    Endpoint to return a book
    @PutMapping("/{id}/return")
    public ResponseEntity<LoanWithFineDto> returnBook(@PathVariable Long id) {
        LoanWithFineDto loanWithFineDto = loanService.returnBook(id);

        return ResponseEntity.ok(loanWithFineDto);
    }

    //    Endpoint to delete a loan for a book
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        loanService.deleteLoan(id);

        return ResponseEntity.noContent().build();
    }

    //    Endpoint to adjust a loan for a book
    @PatchMapping("/{id}")
    public ResponseEntity<LoanDto> patchLoan(@PathVariable Long id, @RequestBody LoanPatchDto loanPatchDto) {
        LoanDto loanDto = loanService.patchLoan(id, loanPatchDto);

        return ResponseEntity.ok(loanDto);
    }
}
