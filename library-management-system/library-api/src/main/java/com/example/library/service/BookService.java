package com.example.library.service;

import com.example.library.entity.Book;
import com.example.library.repository.BookRepository;
import com.example.library.dto.PageResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * BOOK SERVICE - Business logic layer
 * 
 * What is a Service?
 * - Contains business logic and rules
 * - Orchestrates multiple repositories
 * - Validates input data
 * - Handles data transformations
 * - Provides a clean API for controllers
 * 
 * Why separate from Controllers?
 * - Controllers handle HTTP requests
 * - Services handle business operations
 * - Same service can be used by multiple controllers or other services
 * - Easy to test business logic independently
 * - Database changes don't affect business logic
 * 
 * @Service: Spring annotation that:
 * - Marks this as a service component
 * - Creates a singleton instance (one instance for entire app)
 * - Enables @Transactional support (database transactions)
 */
@Service
public class BookService {

    /**
     * Constructor injection (Spring will inject this)
     */
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * CREATE: Add a new book to the library
     * 
     * Best Practices:
     * - Validate input before saving
     * - Set default values
     * - Return the saved entity (includes generated ID)
     */
    public Book createBook(Book book) {
        // Validation (you'll learn more in Phase 3)
        if (book.getTitle() == null || book.getTitle().isBlank()) {
            throw new IllegalArgumentException("Book title cannot be empty");
        }
        if (book.getAuthor() == null || book.getAuthor().isBlank()) {
            throw new IllegalArgumentException("Book author cannot be empty");
        }

        // Set default value
        if (book.getAvailable() == null) {
            book.setAvailable(true);
        }

        // Save and return
        return bookRepository.save(book);
    }

    /**
     * READ: Get all books
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * READ: Get book by ID
     * 
     * Returns Optional because book might not exist
     * - Optional.of(book) if found
     * - Optional.empty() if not found
     * - Prevents NullPointerException
     */
    public Optional<Book> getBookById(Long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be positive");
        }
        return bookRepository.findById(id);
    }

    /**
     * UPDATE: Update existing book
     * 
     * Best Practice:
     * - Check if book exists first
     * - Only update provided fields
     */
    public Optional<Book> updateBook(Long id, Book bookDetails) {
        return bookRepository.findById(id).map(existingBook -> {
            // Only update if new value is provided
            if (bookDetails.getTitle() != null && !bookDetails.getTitle().isBlank()) {
                existingBook.setTitle(bookDetails.getTitle());
            }
            if (bookDetails.getAuthor() != null && !bookDetails.getAuthor().isBlank()) {
                existingBook.setAuthor(bookDetails.getAuthor());
            }
            if (bookDetails.getIsbn() != null && !bookDetails.getIsbn().isBlank()) {
                existingBook.setIsbn(bookDetails.getIsbn());
            }
            if (bookDetails.getAvailable() != null) {
                existingBook.setAvailable(bookDetails.getAvailable());
            }
            return bookRepository.save(existingBook);
        });
    }

    /**
     * DELETE: Remove a book from library
     */
    public boolean deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * SEARCH: Find books by title
     */
    public List<Book> searchByTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Search title cannot be empty");
        }
        return bookRepository.findByTitleIgnoreCase(title);
    }

    /**
     * SEARCH: Find books by author
     */
    public List<Book> searchByAuthor(String author) {
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Author name cannot be empty");
        }
        return bookRepository.findByAuthor(author);
    }

    /**
     * FILTER: Get available books only
     */
    public List<Book> getAvailableBooks() {
        return bookRepository.findByAvailableTrue();
    }

    /**
     * FILTER: Get borrowed books only
     */
    public List<Book> getBorrowedBooks() {
        return bookRepository.findByAvailableFalse();
    }

    /**
     * Get total count of books
     */
    public long getTotalBooks() {
        return bookRepository.count();
    }

    // ===== PHASE 3: ADVANCED SEARCH & PAGINATION =====

    /**
     * Advanced search: Find books by title or author
     */
    public List<Book> searchBooks(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllBooks();
        }
        return bookRepository.searchByTitleOrAuthor(query.trim());
    }

    /**
     * Advanced search: Find books by both title and author
     */
    public List<Book> searchByTitleAndAuthor(String title, String author) {
        if ((title == null || title.trim().isEmpty()) && (author == null || author.trim().isEmpty())) {
            return getAllBooks();
        }
        String titleQuery = title != null ? title.trim() : "";
        String authorQuery = author != null ? author.trim() : "";
        return bookRepository.findByTitleAndAuthor(titleQuery, authorQuery);
    }

    /**
     * Get available books with pagination
     */
    public PageResponse<Book> getAvailableBooksWithPagination(int pageNumber, int pageSize) {
        List<Book> available = bookRepository.findByAvailableTrue();
        return PaginationService.paginate(available, pageNumber, pageSize);
    }

    /**
     * Get unavailable books with pagination
     */
    public PageResponse<Book> getUnavailableBooksWithPagination(int pageNumber, int pageSize) {
        List<Book> unavailable = bookRepository.findByAvailableFalse();
        return PaginationService.paginate(unavailable, pageNumber, pageSize);
    }

    /**
     * Search books with pagination
     */
    public PageResponse<Book> searchBooksWithPagination(String query, int pageNumber, int pageSize) {
        List<Book> results = searchBooks(query);
        return PaginationService.paginate(results, pageNumber, pageSize);
    }

    /**
     * Get books sorted by title with pagination
     */
    public PageResponse<Book> getBooksSortedByTitle(int pageNumber, int pageSize, boolean ascending) {
        List<Book> allBooks = getAllBooks();
        if (ascending) {
            allBooks.sort((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()));
        } else {
            allBooks.sort((a, b) -> b.getTitle().compareToIgnoreCase(a.getTitle()));
        }
        return PaginationService.paginate(allBooks, pageNumber, pageSize);
    }

    /**
     * Get books sorted by author with pagination
     */
    public PageResponse<Book> getBooksSortedByAuthor(int pageNumber, int pageSize, boolean ascending) {
        List<Book> allBooks = getAllBooks();
        if (ascending) {
            allBooks.sort((a, b) -> a.getAuthor().compareToIgnoreCase(b.getAuthor()));
        } else {
            allBooks.sort((a, b) -> b.getAuthor().compareToIgnoreCase(a.getAuthor()));
        }
        return PaginationService.paginate(allBooks, pageNumber, pageSize);
    }
}
