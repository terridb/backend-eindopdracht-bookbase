package com.terrideboer.bookbase.controllers;

import com.terrideboer.bookbase.dtos.fines.FineDto;
import com.terrideboer.bookbase.services.FineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
