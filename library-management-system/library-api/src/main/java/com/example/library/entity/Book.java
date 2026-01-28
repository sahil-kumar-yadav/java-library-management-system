package com.example.library.entity;

import jakarta.persistence.*;
import java.util.List;

/**
 * BOOK ENTITY - Represents a book in the library
 * 
 * @Entity: Marks this class as a JPA entity (will be mapped to a database table)
 * @Table: Specifies the table name in the database
 * 
 * WHY JPA ENTITIES?
 * - Separates database schema from Java objects
 * - Hibernate automatically creates/manages tables
 * - Provides ORM (Object-Relational Mapping)
 * - Makes code database-agnostic
 */
@Entity
@Table(name = "books")
public class Book {

    /**
     * @Id: Marks this field as the primary key
     * @GeneratedValue: Automatically generates unique ID values
     *   - strategy = IDENTITY: Uses database auto-increment
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @Column: Defines column constraints
     * nullable = false: NOT NULL constraint
     * unique = true: UNIQUE constraint (no duplicates)
     */
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "author", nullable = false, length = 100)
    private String author;

    @Column(name = "isbn", unique = true, length = 20)
    private String isbn;

    @Column(name = "available", nullable = false)
    private Boolean available = true;

    /**
     * When this book was added to the library
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private Long createdAt;

    /**
     * One-to-Many: One book can have many loans
     * mappedBy = "book": Student defines the relationship
     * cascade = CascadeType.ALL: Delete loans when book is deleted
     */
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Loan> loans;

    /**
     * One-to-Many: One book can have many reservations
     */
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;

    /**
     * Automatically set the creation timestamp before saving
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = System.currentTimeMillis();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
