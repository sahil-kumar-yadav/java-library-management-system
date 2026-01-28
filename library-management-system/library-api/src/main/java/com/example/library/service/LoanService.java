package com.example.library.service;

import com.example.library.entity.Loan;
import com.example.library.entity.Student;
import com.example.library.entity.Book;
import com.example.library.repository.LoanRepository;
import com.example.library.repository.StudentRepository;
import com.example.library.repository.BookRepository;
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
}
