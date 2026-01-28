package com.example.library.controller;

import com.example.library.entity.Reservation;
import com.example.library.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(
            @RequestParam Long studentId,
            @RequestParam Long bookId) {
        try {
            Reservation reservation = reservationService.createReservation(studentId, bookId);
            return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Reservation>> getReservationsByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(reservationService.getReservationsByStudent(studentId));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<Reservation>> getReservationsByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(reservationService.getReservationsByBook(bookId));
    }

    @GetMapping("/book/{bookId}/active")
    public ResponseEntity<List<Reservation>> getActiveReservationsByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(reservationService.getActiveReservationsByBook(bookId));
    }

    @GetMapping("/active")
    public ResponseEntity<List<Reservation>> getActiveReservations() {
        return ResponseEntity.ok(reservationService.getActiveReservations());
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Reservation> cancelReservation(@PathVariable Long id) {
        try {
            Reservation reservation = reservationService.cancelReservation(id);
            return ResponseEntity.ok(reservation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalReservations() {
        return ResponseEntity.ok(reservationService.getTotalReservations());
    }
}
