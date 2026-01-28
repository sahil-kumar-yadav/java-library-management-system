package com.example.library.repository;

import com.example.library.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * STUDENT REPOSITORY - Database access layer for Student entity
 * 
 * Same pattern as BookRepository:
 * - Extends JpaRepository for CRUD operations
 * - Custom methods for specific queries
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * Find student by email (email is unique)
     */
    Optional<Student> findByEmail(String email);

    /**
     * Find student by roll number (roll number is unique)
     */
    Optional<Student> findByRollNumber(String rollNumber);

    /**
     * Find all active students
     */
    List<Student> findByActiveTrue();

    /**
     * Find all inactive students
     */
    List<Student> findByActiveFalse();

    /**
     * Find students by name (case-insensitive)
     */
    List<Student> findByNameIgnoreCase(String name);
}
