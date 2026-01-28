package com.example.library.controller;

import com.example.library.entity.Loan;
import com.example.library.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<Loan> createLoan(
            @RequestParam Long studentId,
            @RequestParam Long bookId,
            @RequestParam(required = false) String dueDateStr) {
        try {
            LocalDateTime dueDate = dueDateStr != null ? 
                LocalDateTime.parse(dueDateStr) : 
                LocalDateTime.now().plusDays(14);
            
            Loan loan = loanService.createLoan(studentId, bookId, dueDate);
            return ResponseEntity.status(HttpStatus.CREATED).body(loan);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(loanService.getAllLoans());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Loan> getLoanById(@PathVariable Long id) {
        return loanService.getLoanById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Loan>> getLoansByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(loanService.getLoansByStudent(studentId));
    }

    @GetMapping("/student/{studentId}/active")
    public ResponseEntity<List<Loan>> getActiveLoansByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(loanService.getActiveLoansByStudent(studentId));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<Loan>> getLoansByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(loanService.getLoansByBook(bookId));
    }

    @GetMapping("/active")
    public ResponseEntity<List<Loan>> getActiveLoans() {
        return ResponseEntity.ok(loanService.getActiveLoans());
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<Loan>> getOverdueLoans() {
        return ResponseEntity.ok(loanService.getOverdueLoans());
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<Loan> returnBook(@PathVariable Long id) {
        try {
            Loan loan = loanService.returnBook(id);
            return ResponseEntity.ok(loan);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        loanService.deleteLoan(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalLoans() {
        return ResponseEntity.ok(loanService.getTotalLoans());
    }
}
