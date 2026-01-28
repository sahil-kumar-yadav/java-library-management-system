package com.example.library.repository;

import com.example.library.entity.Loan;
import com.example.library.entity.Student;
import com.example.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
