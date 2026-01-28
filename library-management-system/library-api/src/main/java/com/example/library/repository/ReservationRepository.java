package com.example.library.repository;

import com.example.library.entity.Reservation;
import com.example.library.entity.Student;
import com.example.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByStudent(Student student);
    List<Reservation> findByBook(Book book);
    List<Reservation> findByIsCancelledFalse();
    List<Reservation> findByBookAndIsCancelledFalse(Book book);
    Optional<Reservation> findByStudentAndBookAndIsCancelledFalse(Student student, Book book);
}
