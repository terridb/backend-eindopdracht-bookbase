package com.terrideboer.bookbase.controllers;

import com.terrideboer.bookbase.dtos.fines.FineDto;
import com.terrideboer.bookbase.dtos.fines.FineInputDto;
import com.terrideboer.bookbase.dtos.loans.LoanDto;
import com.terrideboer.bookbase.dtos.loans.LoanExtendDto;
import com.terrideboer.bookbase.dtos.loans.LoanInputDto;
import com.terrideboer.bookbase.dtos.loans.LoanWithFineDto;
import com.terrideboer.bookbase.services.FineService;
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
    private final FineService fineService;

    public LoanController(LoanService loanService, FineService fineService) {
        this.loanService = loanService;
        this.fineService = fineService;
    }

    //    Endpoint to get all loans
    @GetMapping
    public ResponseEntity<List<LoanDto>> getAllLoans(
            @RequestParam(required = false) String status
    ) {

        return ResponseEntity.ok(loanService.getAllLoans(status));
    }

    //    Endpoint to get a loan by loan-id
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

    //    Endpoint to manually create a fine for an existing loan by loan-id
    @PostMapping("/{id}/fines")
    public ResponseEntity<FineDto> postManualFine(@PathVariable Long id, @Valid @RequestBody FineInputDto fineInputDto) {
        FineDto fineDto = fineService.postManualFine(fineInputDto, id);

        URI uri = URI.create("/fines/" + fineDto.id);

        return ResponseEntity.created(uri).body(fineDto);
    }

    //    Endpoint to adjust a loan by loan-id (put)
    @PutMapping("/{id}")
    public ResponseEntity<LoanDto> updateLoan(@PathVariable Long id, @Valid @RequestBody LoanInputDto loanInputDto) {
        LoanDto loanDto = loanService.updateLoan(id, loanInputDto);

        return ResponseEntity.ok(loanDto);
    }

    //    Endpoint to return a book by loan-id
    @PatchMapping("/{id}/return")
    public ResponseEntity<LoanWithFineDto> returnBook(@PathVariable Long id) {
        LoanWithFineDto loanWithFineDto = loanService.returnBook(id);

        return ResponseEntity.ok(loanWithFineDto);
    }

    //    Endpoint to extend a loan by loan-id
    @PatchMapping("/{id}/extend")
    public ResponseEntity<LoanDto> extendLoanPeriod(@PathVariable Long id, @Valid @RequestBody LoanExtendDto loanExtendDto) {
        LoanDto loanDto = loanService.extendLoanPeriod(id, loanExtendDto);

        return ResponseEntity.ok(loanDto);
    }

    //    Endpoint to delete a loan by loan-id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        loanService.deleteLoan(id);

        return ResponseEntity.noContent().build();
    }
}
