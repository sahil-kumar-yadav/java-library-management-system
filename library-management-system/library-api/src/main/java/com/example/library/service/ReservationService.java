package com.example.library.service;

import com.example.library.entity.Reservation;
import com.example.library.entity.Student;
import com.example.library.entity.Book;
import com.example.library.repository.ReservationRepository;
import com.example.library.repository.StudentRepository;
import com.example.library.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final StudentRepository studentRepository;
    private final BookRepository bookRepository;

    public ReservationService(ReservationRepository reservationRepository, StudentRepository studentRepository, BookRepository bookRepository) {
        this.reservationRepository = reservationRepository;
        this.studentRepository = studentRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public Reservation createReservation(Long studentId, Long bookId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        // Check if student already has a non-cancelled reservation for this book
        Optional<Reservation> existingReservation = reservationRepository.findByStudentAndBookAndIsCancelledFalse(student, book);
        if (existingReservation.isPresent()) {
            throw new IllegalArgumentException("Student already has a reservation for this book");
        }

        // Get queue position (count of non-cancelled reservations + 1)
        List<Reservation> bookReservations = reservationRepository.findByBookAndIsCancelledFalse(book);
        Integer queuePosition = bookReservations.size() + 1;

        Reservation reservation = new Reservation(student, book, queuePosition);
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    public List<Reservation> getReservationsByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        return reservationRepository.findByStudent(student);
    }

    public List<Reservation> getReservationsByBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        return reservationRepository.findByBook(book);
    }

    public List<Reservation> getActiveReservationsByBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        return reservationRepository.findByBookAndIsCancelledFalse(book);
    }

    @Transactional
    public Reservation cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        if (reservation.getIsCancelled()) {
            throw new IllegalArgumentException("Reservation is already cancelled");
        }

        reservation.setIsCancelled(true);
        reservation.setCancelledDate(LocalDateTime.now());
        
        // Update queue positions for remaining reservations
        updateQueuePositions(reservation.getBook());

        return reservationRepository.save(reservation);
    }

    @Transactional
    public void updateQueuePositions(Book book) {
        List<Reservation> activeReservations = reservationRepository.findByBookAndIsCancelledFalse(book);
        for (int i = 0; i < activeReservations.size(); i++) {
            activeReservations.get(i).setQueuePosition(i + 1);
            reservationRepository.save(activeReservations.get(i));
        }
    }

    @Transactional
    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        reservationRepository.delete(reservation);
    }

    public List<Reservation> getActiveReservations() {
        return reservationRepository.findByIsCancelledFalse();
    }

    public Long getTotalReservations() {
        return reservationRepository.count();
    }
}
