package com.example.library.controller;

import com.example.library.entity.Loan;
import com.example.library.service.LoanService;
import com.example.library.dto.PageResponse;
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
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
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
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
    public ResponseEntity<Loan> returnBook(@PathVariable Long id) {
        try {
            Loan loan = loanService.returnBook(id);
            return ResponseEntity.ok(loan);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        loanService.deleteLoan(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalLoans() {
        return ResponseEntity.ok(loanService.getTotalLoans());
    }

    // ===== PHASE 3: ADVANCED QUERIES & PAGINATION =====

    /**
     * Get active loans sorted by due date
     * HTTP: GET /api/loans/active/sorted
     */
    @GetMapping("/active/sorted")
    public ResponseEntity<List<Loan>> getActiveLoansOrderedByDueDate() {
        return ResponseEntity.ok(loanService.getActiveLoansOrderedByDueDate());
    }

    /**
     * Get loans due within date range
     * HTTP: GET /api/loans/due/between?startDate=2026-01-28T00:00:00&endDate=2026-02-28T00:00:00
     */
    @GetMapping("/due/between")
    public ResponseEntity<List<Loan>> getLoansDueBetweenDates(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDateTime start = LocalDateTime.parse(startDate);
            LocalDateTime end = LocalDateTime.parse(endDate);
            List<Loan> loans = loanService.getLoansDueBetweenDates(start, end);
            return ResponseEntity.ok(loans);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get student's loans sorted
     * HTTP: GET /api/loans/student/{studentId}/sorted
     */
    @GetMapping("/student/{studentId}/sorted")
    public ResponseEntity<List<Loan>> getStudentLoansSorted(@PathVariable Long studentId) {
        try {
            List<Loan> loans = loanService.getStudentLoansSorted(studentId);
            return ResponseEntity.ok(loans);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Count active loans for student
     * HTTP: GET /api/loans/student/{studentId}/count-active
     */
    @GetMapping("/student/{studentId}/count-active")
    public ResponseEntity<Long> countActiveLoansForStudent(@PathVariable Long studentId) {
        try {
            long count = loanService.countActiveLoansForStudent(studentId);
            return ResponseEntity.ok(count);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get recently returned loans
     * HTTP: GET /api/loans/recently-returned
     */
    @GetMapping("/recently-returned")
    public ResponseEntity<List<Loan>> getRecentlyReturnedLoans() {
        return ResponseEntity.ok(loanService.getRecentlyReturnedLoans());
    }

    /**
     * Get loans with pagination
     * HTTP: GET /api/loans/paginated?page=0&size=10
     */
    @GetMapping("/paginated")
    public ResponseEntity<PageResponse<Loan>> getLoansWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<Loan> response = loanService.getLoansWithPagination(page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * Get active loans with pagination
     * HTTP: GET /api/loans/active/paginated?page=0&size=10
     */
    @GetMapping("/active/paginated")
    public ResponseEntity<PageResponse<Loan>> getActiveLoansWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<Loan> response = loanService.getActiveLoansWithPagination(page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * Get overdue loans with pagination
     * HTTP: GET /api/loans/overdue/paginated?page=0&size=10
     */
    @GetMapping("/overdue/paginated")
    public ResponseEntity<PageResponse<Loan>> getOverdueLoansWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<Loan> response = loanService.getOverdueLoansWithPagination(page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * Get student's loans with pagination
     * HTTP: GET /api/loans/student/{studentId}/paginated?page=0&size=10
     */
    @GetMapping("/student/{studentId}/paginated")
    public ResponseEntity<PageResponse<Loan>> getStudentLoansWithPagination(
            @PathVariable Long studentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            PageResponse<Loan> response = loanService.getStudentLoansWithPagination(studentId, page, size);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
