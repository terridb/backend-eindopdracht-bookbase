package com.terrideboer.bookbase.controllers;

import com.terrideboer.bookbase.dtos.loans.LoanWithFineDto;
import com.terrideboer.bookbase.dtos.reservations.ReservationDto;
import com.terrideboer.bookbase.services.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

//    todo add endpoint om alle reserveringen voor één dag op te halen of die ready zijn qua status

    //    Endpoint to get all reservations
    @GetMapping
    public ResponseEntity<List<ReservationDto>> getAllReservations() {

        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    //    Endpoint to get a loan by loan-id
    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto> getReservationById(@PathVariable Long id) {

        return ResponseEntity.ok(reservationService.getReservationById(id));
    }
}
