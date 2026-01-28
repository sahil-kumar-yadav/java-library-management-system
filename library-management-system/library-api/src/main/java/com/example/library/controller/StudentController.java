package com.example.library.controller;

import com.example.library.entity.Student;
import com.example.library.service.StudentService;
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
}
