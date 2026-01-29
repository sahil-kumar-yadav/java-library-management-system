package com.example.library.controller;

import com.example.library.entity.Student;
import com.example.library.service.StudentService;
import com.example.library.dto.PageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * STUDENT REST CONTROLLER - HTTP Request/Response handler for students
 * 
 * Same pattern as BookController:
 * - CRUD endpoints
 * - Proper HTTP status codes
 * - Search and filter endpoints
 */
@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * CREATE: Register a new student
     * 
     * HTTP: POST /api/students
     * Response: 201 CREATED
     */
    @PostMapping
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        try {
            Student createdStudent = studentService.createStudent(student);
            return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * READ: Get all students
     * 
     * HTTP: GET /api/students
     */
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    /**
     * READ: Get student by ID
     * 
     * HTTP: GET /api/students/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Optional<Student> student = studentService.getStudentById(id);
        if (student.isPresent()) {
            return new ResponseEntity<>(student.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * UPDATE: Update student information
     * 
     * HTTP: PUT /api/students/{id}
     */
    @PutMapping("/{id}")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student studentDetails) {
        try {
            Optional<Student> updated = studentService.updateStudent(id, studentDetails);
            if (updated.isPresent()) {
                return new ResponseEntity<>(updated.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * DELETE: Remove a student
     * 
     * HTTP: DELETE /api/students/{id}
     */
    @DeleteMapping("/{id}")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        if (studentService.deleteStudent(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * SEARCH: Get student by email
     * 
     * HTTP: GET /api/students/search/email?q=example@email.com
     */
    @GetMapping("/search/email")
    public ResponseEntity<Student> getStudentByEmail(@RequestParam String q) {
        try {
            Optional<Student> student = studentService.getStudentByEmail(q);
            if (student.isPresent()) {
                return new ResponseEntity<>(student.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * SEARCH: Get student by roll number
     * 
     * HTTP: GET /api/students/search/rollnumber?q=CS001
     */
    @GetMapping("/search/rollnumber")
    public ResponseEntity<Student> getStudentByRollNumber(@RequestParam String q) {
        try {
            Optional<Student> student = studentService.getStudentByRollNumber(q);
            if (student.isPresent()) {
                return new ResponseEntity<>(student.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * FILTER: Get active students
     * 
     * HTTP: GET /api/students/active
     */
    @GetMapping("/active")
    public ResponseEntity<List<Student>> getActiveStudents() {
        List<Student> students = studentService.getActiveStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    /**
     * Get total count of students
     * 
     * HTTP: GET /api/students/count
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalStudents() {
        long count = studentService.getTotalStudents();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    // ===== PHASE 3: ADVANCED SEARCH & PAGINATION =====

    /**
     * Search students by name or email
     * HTTP: GET /api/students/search/query?query=john
     */
    @GetMapping("/search/query")
    public ResponseEntity<List<Student>> searchStudents(@RequestParam String query) {
        try {
            List<Student> results = studentService.searchStudents(query);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Search students by roll number pattern
     * HTTP: GET /api/students/search/rollpattern?pattern=CS
     */
    @GetMapping("/search/rollpattern")
    public ResponseEntity<List<Student>> searchByRollPattern(@RequestParam String pattern) {
        try {
            List<Student> results = studentService.searchByRollNumberPattern(pattern);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get active students with pagination
     * HTTP: GET /api/students/active/paginated?page=0&size=10
     */
    @GetMapping("/active/paginated")
    public ResponseEntity<PageResponse<Student>> getActiveStudentsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<Student> response = studentService.getActiveStudentsWithPagination(page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * Get inactive students with pagination
     * HTTP: GET /api/students/inactive/paginated?page=0&size=10
     */
    @GetMapping("/inactive/paginated")
    public ResponseEntity<PageResponse<Student>> getInactiveStudentsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<Student> response = studentService.getInactiveStudentsWithPagination(page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * Search students with pagination
     * HTTP: GET /api/students/search/paginated?query=alex&page=0&size=10
     */
    @GetMapping("/search/paginated")
    public ResponseEntity<PageResponse<Student>> searchStudentsWithPagination(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<Student> response = studentService.searchStudentsWithPagination(query, page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * Get students sorted by name
     * HTTP: GET /api/students/sorted/name?asc=true&page=0&size=10
     */
    @GetMapping("/sorted/name")
    public ResponseEntity<PageResponse<Student>> getStudentsSortedByName(
            @RequestParam(defaultValue = "true") boolean asc,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<Student> response = studentService.getStudentsSortedByName(page, size, asc);
        return ResponseEntity.ok(response);
    }

    /**
     * Get students sorted by roll number
     * HTTP: GET /api/students/sorted/rollnumber?asc=true&page=0&size=10
     */
    @GetMapping("/sorted/rollnumber")
    public ResponseEntity<PageResponse<Student>> getStudentsSortedByRollNumber(
            @RequestParam(defaultValue = "true") boolean asc,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<Student> response = studentService.getStudentsSortedByRollNumber(page, size, asc);
        return ResponseEntity.ok(response);
    }

    /**
     * Count active students
     * HTTP: GET /api/students/active/count
     */
    @GetMapping("/active/count")
    public ResponseEntity<Long> countActiveStudents() {
        long count = studentService.countActiveStudents();
        return ResponseEntity.ok(count);
    }
}
