package com.example.library.repository;

import com.example.library.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    /**
     * Advanced search: Find students by name or email
     */
    @Query("SELECT s FROM Student s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(s.email) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Student> searchByNameOrEmail(@Param("query") String query);

    /**
     * Advanced search: Find students by roll number pattern
     */
    @Query("SELECT s FROM Student s WHERE s.rollNumber LIKE CONCAT('%', :pattern, '%') ORDER BY s.rollNumber ASC")
    List<Student> findByRollNumberPattern(@Param("pattern") String pattern);

    /**
     * Find active students sorted by name
     */
    @Query("SELECT s FROM Student s WHERE s.active = true ORDER BY s.name ASC")
    List<Student> findActiveStudentsSortedByName();

    /**
     * Find students with specific status
     */
    @Query("SELECT s FROM Student s WHERE s.active = :active ORDER BY s.name ASC")
    List<Student> findByActiveStatus(@Param("active") Boolean active);

    /**
     * Count active students
     */
    @Query("SELECT COUNT(s) FROM Student s WHERE s.active = true")
    long countActiveStudents();
}
