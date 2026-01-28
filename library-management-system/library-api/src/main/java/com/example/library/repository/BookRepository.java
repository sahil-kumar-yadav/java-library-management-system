package com.example.library.repository;

import com.example.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * BOOK REPOSITORY - Database access layer for Book entity
 * 
 * What is a Repository?
 * - The Data Access Layer (DAL)
 * - Handles all database operations for the entity
 * - Encapsulates SQL/database logic
 * - Provides a clean interface for the Service layer
 * 
 * What is JpaRepository?
 * - Spring Data interface that provides common CRUD methods:
 *   * save(entity) - Create or update
 *   * findById(id) - Find by primary key
 *   * findAll() - Get all records
 *   * delete(entity) - Delete a record
 *   * deleteById(id) - Delete by ID
 *   * exists(id) - Check if exists
 *   * count() - Total records
 * 
 * Why use repositories?
 * - Don't repeat database queries across code
 * - Centralize database logic
 * - Easy to test (can mock repositories)
 * - Easy to switch databases (H2 -> MySQL)
 * - Cleaner code (no raw SQL in controllers)
 * 
 * @Repository: Spring annotation that:
 * - Marks this as a Spring component
 * - Enables exception translation (database errors -> Spring exceptions)
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Custom query: Find books by title (case-insensitive)
     * Spring generates SQL automatically based on method name
     * 
     * Method naming convention:
     * findBy + FieldName + IgnoreCase
     */
    List<Book> findByTitleIgnoreCase(String title);

    /**
     * Find books by author
     */
    List<Book> findByAuthor(String author);

    /**
     * Find available books only
     */
    List<Book> findByAvailableTrue();

    /**
     * Find unavailable books (currently borrowed)
     */
    List<Book> findByAvailableFalse();

    /**
     * Find a book by ISBN
     */
    Optional<Book> findByIsbn(String isbn);

    /**
     * Advanced search: Find books by title or author (case-insensitive)
     */
    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(b.author) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Book> searchByTitleOrAuthor(@Param("query") String query);

    /**
     * Advanced search: Find books by title and author combination
     */
    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')) AND LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))")
    List<Book> findByTitleAndAuthor(@Param("title") String title, @Param("author") String author);

    /**
     * Advanced search: Find books with pagination support (manual implementation)
     */
    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')) ORDER BY b.title ASC")
    List<Book> findByTitlePaginated(@Param("title") String title);

    /**
     * Find books with specific availability status sorted by title
     */
    @Query("SELECT b FROM Book b WHERE b.available = :available ORDER BY b.title ASC")
    List<Book> findByAvailabilityStatus(@Param("available") Boolean available);
}
