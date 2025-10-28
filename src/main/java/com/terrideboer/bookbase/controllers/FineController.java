package com.terrideboer.bookbase.controllers;

import com.terrideboer.bookbase.dtos.fines.FineDto;
import com.terrideboer.bookbase.dtos.fines.FineInputDto;
import com.terrideboer.bookbase.services.FineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fines")
public class FineController {

    private final FineService fineService;

    public FineController(FineService fineService) {
        this.fineService = fineService;
    }

    @GetMapping
    public ResponseEntity<List<FineDto>> getAllFines() {

        return ResponseEntity.ok(fineService.getAllFines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FineDto> getFineById(@PathVariable Long id) {

        return ResponseEntity.ok(fineService.getFineById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FineDto> putFine(@PathVariable Long id, @RequestBody FineInputDto fineInputDto) {
        FineDto fineDto = fineService.putFine(id, fineInputDto);

        return ResponseEntity.ok(fineDto);
    }

    @PatchMapping("/{id}/pay")
    public ResponseEntity<FineDto> payFine(@PathVariable Long id) {
        FineDto fineDto = fineService.payFine(id);

        return ResponseEntity.ok(fineDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFine(@PathVariable Long id) {
        fineService.deleteFine(id);

        return ResponseEntity.noContent().build();
    }
}
