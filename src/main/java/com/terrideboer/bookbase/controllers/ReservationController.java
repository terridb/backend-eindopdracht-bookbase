package com.terrideboer.bookbase.controllers;

import com.terrideboer.bookbase.dtos.reservations.ReservationDto;
import com.terrideboer.bookbase.dtos.reservations.ReservationInputDto;
import com.terrideboer.bookbase.dtos.reservations.ReservationPatchDto;
import com.terrideboer.bookbase.services.ReservationService;
import com.terrideboer.bookbase.utils.DateUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    //    Endpoint to get all reservations
    @GetMapping
    public ResponseEntity<List<ReservationDto>> getAllReservations() {

        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    //    Endpoint to get a reservation by reservation-id
    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto> getReservationById(@PathVariable Long id) {

        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    //    Endpoint to get all reservations that need to be prepared
    @GetMapping("/to-prepare")
    public ResponseEntity<List<ReservationDto>> getAllToBePreparedReservations() {

        return ResponseEntity.ok(reservationService.getAllToBePreparedReservations());
    }

    //    Endpoint to download a pdf file with all to prepare reservations
    @GetMapping("/to-prepare/pdf")
    public ResponseEntity<byte[]> downloadReservationsPdf() {
        byte[] pdf = reservationService.generateReservationsPdf();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reservations_" + DateUtils.formatDateTime(LocalDateTime.now()) + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    //    Endpoint to create a new reservation
    @PostMapping
    public ResponseEntity<ReservationDto> postReservation(@Valid @RequestBody ReservationInputDto reservationInputDto) {
        ReservationDto reservationDto = reservationService.postReservation(reservationInputDto);

        URI uri = URI.create("/reservations/" + reservationDto.id);

        return ResponseEntity.created(uri).body(reservationDto);
    }

    //    Endpoint to adjust certain fields of a reservation by reservation-id (patch)
    @PatchMapping("/{id}")
    public ResponseEntity<ReservationDto> patchReservation(@PathVariable Long id, @RequestBody ReservationPatchDto reservationPatchDto) {
        ReservationDto reservationDto = reservationService.patchReservation(id, reservationPatchDto);

        return ResponseEntity.ok(reservationDto);
    }

    //    Endpoint to delete a reservation by reservation-id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);

        return ResponseEntity.noContent().build();
    }

    //    Endpoint to mark a reservation as ready for pickup by reservation-id (patch)
    @PatchMapping("/{id}/ready-for-pickup")
    public ResponseEntity<ReservationDto> markReservationReadyForPickup(@PathVariable Long id) {
        ReservationDto reservationDto = reservationService.markReservationReadyForPickup(id);

        return ResponseEntity.ok(reservationDto);
    }

    //    Endpoint to mark a reservation collected and create a new loan by reservation-id (patch)
    @PatchMapping("/{id}/collect")
    public ResponseEntity<ReservationDto> markReservationAsCollected(@PathVariable Long id) {
        ReservationDto reservationDto = reservationService.markReservationAsCollected(id);

        return ResponseEntity.ok(reservationDto);
    }

    //    Endpoint to cancel a reservation by reservation-id (patch)
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ReservationDto> cancelReservation(@PathVariable Long id) {
        ReservationDto reservationDto = reservationService.cancelReservation(id);

        return ResponseEntity.ok(reservationDto);
    }
}
