package com.example.library.repository;

import com.example.library.entity.Loan;
import com.example.library.entity.Student;
import com.example.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByStudent(Student student);
    List<Loan> findByBook(Book book);
    List<Loan> findByIsReturnedFalse();
    List<Loan> findByIsReturnedTrue();
    List<Loan> findByStudentAndIsReturnedFalse(Student student);
    List<Loan> findByDueDateBeforeAndIsReturnedFalse(LocalDateTime dueDate);
    Optional<Loan> findByStudentAndBookAndIsReturnedFalse(Student student, Book book);

    /**
     * Advanced: Find loans by status sorted by due date
     */
    @Query("SELECT l FROM Loan l WHERE l.isReturned = false ORDER BY l.dueDate ASC")
    List<Loan> findActiveLoansOrderedByDueDate();

    /**
     * Advanced: Find loans overdue by number of days
     */
    @Query("SELECT l FROM Loan l WHERE l.dueDate < :cutoffDate AND l.isReturned = false ORDER BY l.dueDate ASC")
    List<Loan> findOverdueLoansBeforeDate(@Param("cutoffDate") LocalDateTime cutoffDate);

    /**
     * Advanced: Find loans by student sorted by due date
     */
    @Query("SELECT l FROM Loan l WHERE l.student.id = :studentId ORDER BY l.loanDate DESC")
    List<Loan> findLoansByStudentSorted(@Param("studentId") Long studentId);

    /**
     * Advanced: Count active loans for a student
     */
    @Query("SELECT COUNT(l) FROM Loan l WHERE l.student.id = :studentId AND l.isReturned = false")
    long countActiveLoansForStudent(@Param("studentId") Long studentId);

    /**
     * Advanced: Find recently returned loans
     */
    @Query("SELECT l FROM Loan l WHERE l.isReturned = true ORDER BY l.returnDate DESC")
    List<Loan> findRecentlyReturnedLoans();

    /**
     * Advanced: Find loans due within X days
     */
    @Query("SELECT l FROM Loan l WHERE l.isReturned = false AND l.dueDate BETWEEN :startDate AND :endDate ORDER BY l.dueDate ASC")
    List<Loan> findLoansDueBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
