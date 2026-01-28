package com.example.library.controller;

import com.example.library.entity.Book;
import com.example.library.service.BookService;
import com.example.library.dto.PageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * BOOK REST CONTROLLER - HTTP Request/Response handler for books
 * 
 * What is a REST Controller?
 * - Handles HTTP requests (GET, POST, PUT, DELETE)
 * - Converts request data to Java objects
 * - Calls service methods for business logic
 * - Converts response data to JSON
 * - Returns appropriate HTTP status codes
 * 
 * What is REST?
 * Representational State Transfer - Architecture style for APIs:
 * - Resources: /api/books (plural nouns)
 * - Operations: GET, POST, PUT, DELETE (HTTP methods)
 * - State: Represented by HTTP status codes
 * - Format: Usually JSON
 * 
 * @RestController: Combines @Controller + @ResponseBody
 * - Marks this as a REST endpoint handler
 * - Automatically converts responses to JSON
 * 
 * @RequestMapping: Base path for all endpoints
 * - All endpoints will start with /api/books
 */
@RestController
@RequestMapping("/books")
public class BookController {

    /**
     * Injected by Spring
     */
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * CREATE: Add a new book
     * 
     * HTTP: POST /api/books
     * Request Body: {"title": "...", "author": "..."}
     * Response: 201 CREATED + Book object with generated ID
     * 
     * @RequestBody: Converts JSON request body to Book object
     * ResponseEntity: Allows setting HTTP status code
     */
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        try {
            Book createdBook = bookService.createBook(book);
            // 201 Created: Resource was successfully created
            return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // 400 Bad Request: Invalid input
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * READ: Get all books
     * 
     * HTTP: GET /api/books
     * Response: 200 OK + List of all books
     */
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    /**
     * READ: Get a specific book by ID
     * 
     * HTTP: GET /api/books/{id}
     * Example: GET /api/books/1
     * Response: 200 OK + Book object OR 404 NOT FOUND
     * 
     * @PathVariable: Extracts {id} from URL path
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookService.getBookById(id);
        if (book.isPresent()) {
            // 200 OK
            return new ResponseEntity<>(book.get(), HttpStatus.OK);
        } else {
            // 404 Not Found: Resource doesn't exist
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * UPDATE: Modify an existing book
     * 
     * HTTP: PUT /api/books/{id}
     * Request Body: {"title": "...", "author": "..."}
     * Response: 200 OK + Updated book OR 404 NOT FOUND
     * 
     * Best Practice:
     * - Use PUT for full/partial updates
     * - Idempotent: Same request = same result
     */
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
        try {
            Optional<Book> updated = bookService.updateBook(id, bookDetails);
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
     * DELETE: Remove a book
     * 
     * HTTP: DELETE /api/books/{id}
     * Response: 204 NO CONTENT (success, no body) OR 404 NOT FOUND
     * 
     * Note:
     * - 204 No Content: Success but no response body
     * - 200 OK: Success with response body
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if (bookService.deleteBook(id)) {
            // 204 No Content: Successfully deleted
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * SEARCH: Find books by title
     * 
     * HTTP: GET /api/books/search/title?q=Java
     * Response: 200 OK + List of matching books
     * 
     * @RequestParam: Extracts query parameter (?q=...)
     */
    @GetMapping("/search/title")
    public ResponseEntity<List<Book>> searchByTitle(@RequestParam String q) {
        try {
            List<Book> books = bookService.searchByTitle(q);
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * SEARCH: Find books by author
     * 
     * HTTP: GET /api/books/search/author?q=Author%20Name
     */
    @GetMapping("/search/author")
    public ResponseEntity<List<Book>> searchByAuthor(@RequestParam String q) {
        try {
            List<Book> books = bookService.searchByAuthor(q);
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * FILTER: Get available books
     * 
     * HTTP: GET /api/books/available
     * Response: 200 OK + List of available books
     */
    @GetMapping("/available")
    public ResponseEntity<List<Book>> getAvailableBooks() {
        List<Book> books = bookService.getAvailableBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    /**
     * FILTER: Get borrowed books
     * 
     * HTTP: GET /api/books/borrowed
     */
    @GetMapping("/borrowed")
    public ResponseEntity<List<Book>> getBorrowedBooks() {
        List<Book> books = bookService.getBorrowedBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    /**
     * Get total count of books
     * 
     * HTTP: GET /api/books/count
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalBooks() {
        long count = bookService.getTotalBooks();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    // ===== PHASE 3: ADVANCED SEARCH & PAGINATION =====

    /**
     * Search books by title or author
     * HTTP: GET /api/books/search?query=spring
     */
    @GetMapping("/search/query")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam String query) {
        try {
            List<Book> results = bookService.searchBooks(query);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Search books by title AND author
     * HTTP: GET /api/books/search/advanced?title=Spring&author=Walls
     */
    @GetMapping("/search/advanced")
    public ResponseEntity<List<Book>> searchByTitleAndAuthor(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author) {
        try {
            List<Book> results = bookService.searchByTitleAndAuthor(title, author);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get available books with pagination
     * HTTP: GET /api/books/available/paginated?page=0&size=10
     */
    @GetMapping("/available/paginated")
    public ResponseEntity<PageResponse<Book>> getAvailableBooksWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<Book> response = bookService.getAvailableBooksWithPagination(page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * Get unavailable books with pagination
     * HTTP: GET /api/books/unavailable/paginated?page=0&size=10
     */
    @GetMapping("/unavailable/paginated")
    public ResponseEntity<PageResponse<Book>> getUnavailableBooksWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<Book> response = bookService.getUnavailableBooksWithPagination(page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * Search books with pagination
     * HTTP: GET /api/books/search/paginated?query=java&page=0&size=10
     */
    @GetMapping("/search/paginated")
    public ResponseEntity<PageResponse<Book>> searchBooksWithPagination(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<Book> response = bookService.searchBooksWithPagination(query, page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * Get books sorted by title
     * HTTP: GET /api/books/sorted/title?asc=true&page=0&size=10
     */
    @GetMapping("/sorted/title")
    public ResponseEntity<PageResponse<Book>> getBooksSortedByTitle(
            @RequestParam(defaultValue = "true") boolean asc,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<Book> response = bookService.getBooksSortedByTitle(page, size, asc);
        return ResponseEntity.ok(response);
    }

    /**
     * Get books sorted by author
     * HTTP: GET /api/books/sorted/author?asc=true&page=0&size=10
     */
    @GetMapping("/sorted/author")
    public ResponseEntity<PageResponse<Book>> getBooksSortedByAuthor(
            @RequestParam(defaultValue = "true") boolean asc,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<Book> response = bookService.getBooksSortedByAuthor(page, size, asc);
        return ResponseEntity.ok(response);
    }
}
