package com.terrideboer.bookbase.controllers;

import com.terrideboer.bookbase.dtos.loans.LoanDto;
import com.terrideboer.bookbase.dtos.loans.LoanInputDto;
import com.terrideboer.bookbase.dtos.loans.LoanWithFineDto;
import com.terrideboer.bookbase.dtos.reservations.ReservationDto;
import com.terrideboer.bookbase.dtos.reservations.ReservationInputDto;
import com.terrideboer.bookbase.dtos.reservations.ReservationPatchDto;
import com.terrideboer.bookbase.services.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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

    //    Endpoint to create a new reservation
    @PostMapping
    public ResponseEntity<ReservationDto> postReservation(@Valid @RequestBody ReservationInputDto reservationInputDto) {
        ReservationDto reservationDto = reservationService.postReservation(reservationInputDto);

        URI uri = URI.create("/reservations/" + reservationDto.id);

        return ResponseEntity.created(uri).body(reservationDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReservationDto> patchReservation(@PathVariable Long id, @RequestBody ReservationPatchDto reservationPatchDto) {
        ReservationDto reservationDto = reservationService.patchReservation(id, reservationPatchDto);

        return ResponseEntity.ok(reservationDto);
    }
}
