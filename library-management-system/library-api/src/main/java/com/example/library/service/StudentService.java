package com.example.library.service;

import com.example.library.entity.Student;
import com.example.library.repository.StudentRepository;
import com.example.library.dto.PageResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * STUDENT SERVICE - Business logic for student management
 * 
 * Same pattern as BookService:
 * - CRUD operations
 * - Business logic
 * - Input validation
 */
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * CREATE: Register a new student
     */
    public Student createStudent(Student student) {
        // Validation
        if (student.getName() == null || student.getName().isBlank()) {
            throw new IllegalArgumentException("Student name cannot be empty");
        }
        if (student.getEmail() == null || student.getEmail().isBlank()) {
            throw new IllegalArgumentException("Student email cannot be empty");
        }
        if (student.getRollNumber() == null || student.getRollNumber().isBlank()) {
            throw new IllegalArgumentException("Student roll number cannot be empty");
        }

        // Check if email or roll number already exists
        if (studentRepository.findByEmail(student.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }
        if (studentRepository.findByRollNumber(student.getRollNumber()).isPresent()) {
            throw new IllegalArgumentException("Roll number already registered");
        }

        // Set default
        if (student.getActive() == null) {
            student.setActive(true);
        }

        return studentRepository.save(student);
    }

    /**
     * READ: Get all students
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * READ: Get student by ID
     */
    public Optional<Student> getStudentById(Long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be positive");
        }
        return studentRepository.findById(id);
    }

    /**
     * UPDATE: Update student information
     */
    public Optional<Student> updateStudent(Long id, Student studentDetails) {
        return studentRepository.findById(id).map(existingStudent -> {
            if (studentDetails.getName() != null && !studentDetails.getName().isBlank()) {
                existingStudent.setName(studentDetails.getName());
            }
            if (studentDetails.getEmail() != null && !studentDetails.getEmail().isBlank()) {
                existingStudent.setEmail(studentDetails.getEmail());
            }
            if (studentDetails.getActive() != null) {
                existingStudent.setActive(studentDetails.getActive());
            }
            return studentRepository.save(existingStudent);
        });
    }

    /**
     * DELETE: Remove a student (deactivate or delete)
     */
    public boolean deleteStudent(Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * SEARCH: Find student by email
     */
    public Optional<Student> getStudentByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        return studentRepository.findByEmail(email);
    }

    /**
     * SEARCH: Find student by roll number
     */
    public Optional<Student> getStudentByRollNumber(String rollNumber) {
        if (rollNumber == null || rollNumber.isBlank()) {
            throw new IllegalArgumentException("Roll number cannot be empty");
        }
        return studentRepository.findByRollNumber(rollNumber);
    }

    /**
     * FILTER: Get active students
     */
    public List<Student> getActiveStudents() {
        return studentRepository.findByActiveTrue();
    }

    /**
     * Get total count of students
     */
    public long getTotalStudents() {
        return studentRepository.count();
    }

    // ===== PHASE 3: ADVANCED SEARCH & PAGINATION =====

    /**
     * Advanced search: Find students by name or email
     */
    public List<Student> searchStudents(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllStudents();
        }
        return studentRepository.searchByNameOrEmail(query.trim());
    }

    /**
     * Advanced search: Find students by roll number pattern
     */
    public List<Student> searchByRollNumberPattern(String pattern) {
        if (pattern == null || pattern.trim().isEmpty()) {
            return getAllStudents();
        }
        return studentRepository.findByRollNumberPattern(pattern.trim());
    }

    /**
     * Get active students with pagination
     */
    public PageResponse<Student> getActiveStudentsWithPagination(int pageNumber, int pageSize) {
        List<Student> active = studentRepository.findByActiveTrue();
        return PaginationService.paginate(active, pageNumber, pageSize);
    }

    /**
     * Get inactive students with pagination
     */
    public PageResponse<Student> getInactiveStudentsWithPagination(int pageNumber, int pageSize) {
        List<Student> inactive = studentRepository.findByActiveFalse();
        return PaginationService.paginate(inactive, pageNumber, pageSize);
    }

    /**
     * Search students with pagination
     */
    public PageResponse<Student> searchStudentsWithPagination(String query, int pageNumber, int pageSize) {
        List<Student> results = searchStudents(query);
        return PaginationService.paginate(results, pageNumber, pageSize);
    }

    /**
     * Get students sorted by name with pagination
     */
    public PageResponse<Student> getStudentsSortedByName(int pageNumber, int pageSize, boolean ascending) {
        List<Student> allStudents = getAllStudents();
        if (ascending) {
            allStudents.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        } else {
            allStudents.sort((a, b) -> b.getName().compareToIgnoreCase(a.getName()));
        }
        return PaginationService.paginate(allStudents, pageNumber, pageSize);
    }

    /**
     * Get students sorted by roll number with pagination
     */
    public PageResponse<Student> getStudentsSortedByRollNumber(int pageNumber, int pageSize, boolean ascending) {
        List<Student> allStudents = getAllStudents();
        if (ascending) {
            allStudents.sort((a, b) -> a.getRollNumber().compareToIgnoreCase(b.getRollNumber()));
        } else {
            allStudents.sort((a, b) -> b.getRollNumber().compareToIgnoreCase(a.getRollNumber()));
        }
        return PaginationService.paginate(allStudents, pageNumber, pageSize);
    }

    /**
     * Count active students
     */
    public long countActiveStudents() {
        return studentRepository.countActiveStudents();
    }
}
