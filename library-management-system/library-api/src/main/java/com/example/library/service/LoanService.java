package com.example.library.service;

import com.example.library.entity.Loan;
import com.example.library.entity.Student;
import com.example.library.entity.Book;
import com.example.library.repository.LoanRepository;
import com.example.library.repository.StudentRepository;
import com.example.library.repository.BookRepository;
import com.example.library.dto.PageResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {
    private final LoanRepository loanRepository;
    private final StudentRepository studentRepository;
    private final BookRepository bookRepository;

    public LoanService(LoanRepository loanRepository, StudentRepository studentRepository, BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.studentRepository = studentRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public Loan createLoan(Long studentId, Long bookId, LocalDateTime dueDate) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        if (!book.getAvailable()) {
            throw new IllegalArgumentException("Book is not available for borrowing");
        }

        // Check if student already has this book borrowed
        Optional<Loan> existingLoan = loanRepository.findByStudentAndBookAndIsReturnedFalse(student, book);
        if (existingLoan.isPresent()) {
            throw new IllegalArgumentException("Student already has this book borrowed");
        }

        Loan loan = new Loan(student, book, dueDate);
        book.setAvailable(false);
        bookRepository.save(book);
        return loanRepository.save(loan);
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public Optional<Loan> getLoanById(Long id) {
        return loanRepository.findById(id);
    }

    public List<Loan> getLoansByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        return loanRepository.findByStudent(student);
    }

    public List<Loan> getLoansByBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        return loanRepository.findByBook(book);
    }

    public List<Loan> getActiveLoansByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        return loanRepository.findByStudentAndIsReturnedFalse(student);
    }

    public List<Loan> getOverdueLoans() {
        return loanRepository.findByDueDateBeforeAndIsReturnedFalse(LocalDateTime.now());
    }

    @Transactional
    public Loan returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        if (loan.getIsReturned()) {
            throw new IllegalArgumentException("Book is already returned");
        }

        loan.setIsReturned(true);
        loan.setReturnDate(LocalDateTime.now());
        
        Book book = loan.getBook();
        book.setAvailable(true);
        bookRepository.save(book);

        return loanRepository.save(loan);
    }

    @Transactional
    public void deleteLoan(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));
        loanRepository.delete(loan);
    }

    public List<Loan> getActiveLoans() {
        return loanRepository.findByIsReturnedFalse();
    }

    public Long getTotalLoans() {
        return loanRepository.count();
    }

    // ===== PHASE 3: ADVANCED QUERIES & PAGINATION =====

    /**
     * Get active loans sorted by due date
     */
    public List<Loan> getActiveLoansOrderedByDueDate() {
        return loanRepository.findActiveLoansOrderedByDueDate();
    }

    /**
     * Get loans overdue by specific date
     */
    public List<Loan> getOverdueLoansBeforeDate(LocalDateTime cutoffDate) {
        return loanRepository.findOverdueLoansBeforeDate(cutoffDate);
    }

    /**
     * Get student's loans sorted by loan date
     */
    public List<Loan> getStudentLoansSorted(Long studentId) {
        studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        return loanRepository.findLoansByStudentSorted(studentId);
    }

    /**
     * Count active loans for a student
     */
    public long countActiveLoansForStudent(Long studentId) {
        studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        return loanRepository.countActiveLoansForStudent(studentId);
    }

    /**
     * Get recently returned loans
     */
    public List<Loan> getRecentlyReturnedLoans() {
        return loanRepository.findRecentlyReturnedLoans();
    }

    /**
     * Get loans due within date range
     */
    public List<Loan> getLoansDueBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return loanRepository.findLoansDueBetweenDates(startDate, endDate);
    }

    /**
     * Get loans with pagination
     */
    public PageResponse<Loan> getLoansWithPagination(int pageNumber, int pageSize) {
        List<Loan> allLoans = getAllLoans();
        return PaginationService.paginate(allLoans, pageNumber, pageSize);
    }

    /**
     * Get active loans with pagination
     */
    public PageResponse<Loan> getActiveLoansWithPagination(int pageNumber, int pageSize) {
        List<Loan> activeLoans = getActiveLoans();
        return PaginationService.paginate(activeLoans, pageNumber, pageSize);
    }

    /**
     * Get overdue loans with pagination
     */
    public PageResponse<Loan> getOverdueLoansWithPagination(int pageNumber, int pageSize) {
        List<Loan> overdueLoans = getOverdueLoans();
        return PaginationService.paginate(overdueLoans, pageNumber, pageSize);
    }

    /**
     * Get loans for student with pagination
     */
    public PageResponse<Loan> getStudentLoansWithPagination(Long studentId, int pageNumber, int pageSize) {
        List<Loan> studentLoans = getLoansByStudent(studentId);
        return PaginationService.paginate(studentLoans, pageNumber, pageSize);
    }
}
