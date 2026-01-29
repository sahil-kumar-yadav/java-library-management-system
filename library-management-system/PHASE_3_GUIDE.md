# Phase 3: Advanced Queries & Pagination

## 🎯 Overview

Phase 3 adds **advanced search, filtering, and pagination** capabilities to the library system. This phase teaches you:

- **Custom JPQL Queries** (Query Language for databases)
- **Advanced Search** (by multiple fields, patterns)
- **Pagination** (limit results, browse large datasets)
- **Sorting** (order results by different criteria)
- **Data Transfer Objects** (DTOs for complex responses)
- **Query Optimization** (efficient database queries)

## 🆕 New Features

### 1. PageResponse DTO
Custom response object for paginated results:
```java
{
  "content": [ /* 10 items */ ],
  "pageNumber": 0,
  "pageSize": 10,
  "totalElements": 450,
  "totalPages": 45,
  "hasNext": true,
  "hasPrevious": false
}
```

### 2. Custom JPQL Queries
Search and filter using SQL-like queries:
```java
@Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(b.author) LIKE LOWER(CONCAT('%', :query, '%'))")
List<Book> searchByTitleOrAuthor(@Param("query") String query);
```

### 3. Advanced Search Endpoints
Multiple ways to find data:
- **Title/Author Search** - Search books by text
- **Pattern Matching** - Find roll numbers starting with "CS"
- **Multi-field Search** - Search name AND email simultaneously
- **Status Filtering** - Find active/inactive students

### 4. Pagination Support
Browse large result sets:
- Page-based (page 0, 10 items per page)
- Get total count without loading all data
- Navigate with hasNext/hasPrevious flags

### 5. Sorting Options
Order results:
- By title (A-Z or Z-A)
- By author (ascending/descending)
- By name
- By roll number

## 📊 New Repositories - Custom Queries

### BookRepository (4 new JPQL queries)
```java
// Search by title OR author
@Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(b.author) LIKE LOWER(CONCAT('%', :query, '%'))")
List<Book> searchByTitleOrAuthor(@Param("query") String query);

// Search by title AND author
@Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')) AND LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))")
List<Book> findByTitleAndAuthor(@Param("title") String title, @Param("author") String author);

// Find with pagination support
@Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')) ORDER BY b.title ASC")
List<Book> findByTitlePaginated(@Param("title") String title);

// Find by availability status (sorted)
@Query("SELECT b FROM Book b WHERE b.available = :available ORDER BY b.title ASC")
List<Book> findByAvailabilityStatus(@Param("available") Boolean available);
```

### StudentRepository (6 new JPQL queries)
```java
// Search by name OR email
@Query("SELECT s FROM Student s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(s.email) LIKE LOWER(CONCAT('%', :query, '%'))")
List<Student> searchByNameOrEmail(@Param("query") String query);

// Pattern matching on roll number
@Query("SELECT s FROM Student s WHERE s.rollNumber LIKE CONCAT('%', :pattern, '%') ORDER BY s.rollNumber ASC")
List<Student> findByRollNumberPattern(@Param("pattern") String pattern);

// Active students sorted
@Query("SELECT s FROM Student s WHERE s.active = true ORDER BY s.name ASC")
List<Student> findActiveStudentsSortedByName();

// Status-based filtering
@Query("SELECT s FROM Student s WHERE s.active = :active ORDER BY s.name ASC")
List<Student> findByActiveStatus(@Param("active") Boolean active);

// Count active students
@Query("SELECT COUNT(s) FROM Student s WHERE s.active = true")
long countActiveStudents();
```

### LoanRepository (7 new JPQL queries)
```java
// Active loans by due date
@Query("SELECT l FROM Loan l WHERE l.isReturned = false ORDER BY l.dueDate ASC")
List<Loan> findActiveLoansOrderedByDueDate();

// Overdue loans before date
@Query("SELECT l FROM Loan l WHERE l.dueDate < :cutoffDate AND l.isReturned = false ORDER BY l.dueDate ASC")
List<Loan> findOverdueLoansBeforeDate(@Param("cutoffDate") LocalDateTime cutoffDate);

// Loans by student (sorted)
@Query("SELECT l FROM Loan l WHERE l.student.id = :studentId ORDER BY l.loanDate DESC")
List<Loan> findLoansByStudentSorted(@Param("studentId") Long studentId);

// Count active loans
@Query("SELECT COUNT(l) FROM Loan l WHERE l.student.id = :studentId AND l.isReturned = false")
long countActiveLoansForStudent(@Param("studentId") Long studentId);

// Recently returned
@Query("SELECT l FROM Loan l WHERE l.isReturned = true ORDER BY l.returnDate DESC")
List<Loan> findRecentlyReturnedLoans();

// Loans due within date range
@Query("SELECT l FROM Loan l WHERE l.isReturned = false AND l.dueDate BETWEEN :startDate AND :endDate ORDER BY l.dueDate ASC")
List<Loan> findLoansDueBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
```

## 📋 New API Endpoints - 30+ Total

### Book Search & Pagination (6 endpoints)
```
GET    /api/books/search/query?query=spring           # Search by text
GET    /api/books/search/advanced?title=X&author=Y     # Multi-field search
GET    /api/books/available/paginated?page=0&size=10   # Available with pagination
GET    /api/books/unavailable/paginated?page=0&size=10 # Unavailable with pagination
GET    /api/books/search/paginated?query=X&page=0&size=10
GET    /api/books/sorted/title?asc=true&page=0&size=10 # Sort by title
GET    /api/books/sorted/author?asc=true&page=0&size=10 # Sort by author
```

### Student Search & Pagination (7 endpoints)
```
GET    /api/students/search/query?query=john           # Search by text
GET    /api/students/search/rollpattern?pattern=CS     # Roll number pattern
GET    /api/students/active/paginated?page=0&size=10   # Active with pagination
GET    /api/students/inactive/paginated?page=0&size=10 # Inactive with pagination
GET    /api/students/search/paginated?query=X&page=0&size=10
GET    /api/students/sorted/name?asc=true&page=0&size=10 # Sort by name
GET    /api/students/sorted/rollnumber?asc=true&page=0&size=10 # Sort by roll
GET    /api/students/active/count                       # Count active students
```

### Loan Advanced Queries (8 endpoints)
```
GET    /api/loans/active/sorted                        # Active sorted by due date
GET    /api/loans/due/between?startDate=X&endDate=Y    # Due within date range
GET    /api/loans/student/{studentId}/sorted           # Student loans sorted
GET    /api/loans/student/{studentId}/count-active     # Count student's active loans
GET    /api/loans/recently-returned                    # Recently returned loans
GET    /api/loans/paginated?page=0&size=10            # All loans with pagination
GET    /api/loans/active/paginated?page=0&size=10     # Active with pagination
GET    /api/loans/overdue/paginated?page=0&size=10    # Overdue with pagination
GET    /api/loans/student/{studentId}/paginated       # Student loans paginated
```

## 🔧 Service Layer - Pagination Support

### PaginationService Helper
```java
public class PaginationService {
    // Paginate any list
    public static <T> PageResponse<T> paginate(
        List<T> allItems, 
        int pageNumber, 
        int pageSize) { ... }
    
    // Sort and paginate
    public static <T extends Comparable<T>> PageResponse<T> sortAndPaginate(
        List<T> allItems, 
        int pageNumber, 
        int pageSize, 
        boolean ascending) { ... }
}
```

### BookService (7 new methods)
```java
// Advanced search
List<Book> searchBooks(String query)
List<Book> searchByTitleAndAuthor(String title, String author)

// Pagination methods
PageResponse<Book> getAvailableBooksWithPagination(int page, int size)
PageResponse<Book> getUnavailableBooksWithPagination(int page, int size)
PageResponse<Book> searchBooksWithPagination(String query, int page, int size)

// Sorting with pagination
PageResponse<Book> getBooksSortedByTitle(int page, int size, boolean asc)
PageResponse<Book> getBooksSortedByAuthor(int page, int size, boolean asc)
```

### StudentService (7 new methods)
```java
// Advanced search
List<Student> searchStudents(String query)
List<Student> searchByRollNumberPattern(String pattern)

// Pagination methods
PageResponse<Student> getActiveStudentsWithPagination(int page, int size)
PageResponse<Student> getInactiveStudentsWithPagination(int page, int size)
PageResponse<Student> searchStudentsWithPagination(String query, int page, int size)

// Sorting with pagination
PageResponse<Student> getStudentsSortedByName(int page, int size, boolean asc)
PageResponse<Student> getStudentsSortedByRollNumber(int page, int size, boolean asc)

// Additional
long countActiveStudents()
```

### LoanService (8 new methods)
```java
// Advanced queries
List<Loan> getActiveLoansOrderedByDueDate()
List<Loan> getOverdueLoansBeforeDate(LocalDateTime cutoffDate)
List<Loan> getStudentLoansSorted(Long studentId)
long countActiveLoansForStudent(Long studentId)
List<Loan> getRecentlyReturnedLoans()
List<Loan> getLoansDueBetweenDates(LocalDateTime start, LocalDateTime end)

// Pagination methods
PageResponse<Loan> getLoansWithPagination(int page, int size)
PageResponse<Loan> getActiveLoansWithPagination(int page, int size)
PageResponse<Loan> getOverdueLoansWithPagination(int page, int size)
PageResponse<Loan> getStudentLoansWithPagination(Long studentId, int page, int size)
```

## 🔍 Query Examples

### Search Books by Title or Author
```bash
curl "https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/books/search/query?query=spring"
```

**Response:**
```json
[
  {
    "id": 1,
    "title": "Spring in Action",
    "author": "Craig Walls",
    "isbn": "...",
    "available": true
  },
  {
    "id": 2,
    "title": "Spring Boot Up and Running",
    "author": "Mark Heckler",
    "isbn": "...",
    "available": false
  }
]
```

### Get Available Books with Pagination
```bash
curl "https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/books/available/paginated?page=0&size=10"
```

**Response:**
```json
{
  "content": [ /* 10 books */ ],
  "pageNumber": 0,
  "pageSize": 10,
  "totalElements": 45,
  "totalPages": 5,
  "hasNext": true,
  "hasPrevious": false
}
```

### Search Active Students with Pagination
```bash
curl "https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/students/search/paginated?query=alex&page=0&size=10"
```

### Get Loans Due This Week
```bash
curl "https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/loans/due/between?startDate=2026-01-28T00:00:00&endDate=2026-02-04T23:59:59"
```

### Get Student's Active Loans Count
```bash
curl "https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/loans/student/1/count-active"
```

**Response:**
```json
3
```

### Sort Books by Author Descending with Pagination
```bash
curl "https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/books/sorted/author?asc=false&page=0&size=10"
```

## 🧪 Testing Guide

### Test 1: Search for Books
```bash
# Add some books first
curl -X POST https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/books \
  -H "Content-Type: application/json" \
  -d '{"title":"Spring Boot Guide","author":"Mark Heckler","isbn":"123"}'

# Search
curl "https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/books/search/query?query=spring"
```

### Test 2: Pagination
```bash
# Get first page
curl "https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/books/available/paginated?page=0&size=5"

# Get second page
curl "https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/books/available/paginated?page=1&size=5"
```

### Test 3: Sorting
```bash
# Sort ascending (A-Z)
curl "https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/books/sorted/title?asc=true&page=0&size=10"

# Sort descending (Z-A)
curl "https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/books/sorted/title?asc=false&page=0&size=10"
```

### Test 4: Advanced Student Search
```bash
# Search by pattern
curl "https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/students/search/rollpattern?pattern=CS"

# Count active
curl "https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/students/active/count"
```

## 🎓 Key Concepts Learned

### 1. JPQL (Java Persistence Query Language)
- SQL-like language for JPA queries
- Works across different databases
- Type-safe queries
- Parameter binding with @Param

### 2. Custom Query Annotations
```java
@Query("SELECT ... FROM ... WHERE ...")  // Custom JPQL
@Param("paramName")                       // Bind parameters
```

### 3. Pagination Pattern
```
Input: page=0, size=10
Process: Skip (0 * 10), Take 10 items
Output: Items 0-9, hasNext=true
```

### 4. DTO Pattern
- Separate response objects
- Hide internal details
- Custom formatting for API responses

### 5. Generic Programming
```java
<T> PageResponse<T> paginate(List<T> items, int page, int size)
// Works with any type: Book, Student, Loan, etc.
```

## 📈 Performance Considerations

### Problem: Loading All Data
```java
// BAD - Loads all 1000 books
List<Book> books = bookRepository.findAll();
List<Book> page1 = books.subList(0, 10);
```

### Solution: Database Pagination
```java
// GOOD - Queries only 10 rows
@Query("SELECT b FROM Book b ORDER BY b.title LIMIT 10 OFFSET 0")
List<Book> getPage1();
```

**Current Implementation:**
- Custom JPQL queries limit results in database
- Manual pagination for sorting
- Efficient for most datasets

**Future Optimization:**
- Use Spring Data's Pageable interface
- Add database indexes on search fields
- Implement full-text search (Elasticsearch)
- Add result caching

## 🔄 Workflow Examples

### Find Books and Display Page-by-Page
```
1. User searches for "spring"
2. API: GET /api/books/search/paginated?query=spring&page=0&size=10
3. Return: 10 results + totalPages=5
4. UI: Show page 1 of 5, "Next" button enabled
5. User clicks "Next"
6. API: GET /api/books/search/paginated?query=spring&page=1&size=10
7. Return: Next 10 results
```

### Filter and Sort Student List
```
1. Librarian clicks "Active Students, sorted by name"
2. API: GET /api/students/sorted/name?asc=true&page=0&size=20
3. Return: 20 students sorted A-Z
4. Librarian pagination: page 0, 1, 2, ...
```

### Track Student's Due Loans
```
1. Student ID = 1
2. API: GET /api/loans/student/1/sorted
3. Return: All loans for Student 1, sorted by loan date
4. Find ones with isReturned=false (active)
5. Check dueDate to find overdue ones
```

## ✅ Summary - Phase 3 Complete

**What You Built:**
- ✅ Advanced search across multiple fields
- ✅ Pagination for large result sets
- ✅ Sorting by multiple criteria
- ✅ Custom JPQL queries
- ✅ 30+ new API endpoints
- ✅ Pagination DTOs
- ✅ Helper utilities

**What You Learned:**
- Query language (JPQL)
- Database optimization
- Pagination design patterns
- Generic programming
- API design for filtering/sorting

**Next Phase: Authentication & Security (Phase 4)**
- User login/logout
- Role-based access control (Student, Librarian, Admin)
- JWT tokens
- Password encryption

