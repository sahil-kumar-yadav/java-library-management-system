# Phase 2: Database Relationships - Loans & Reservations

## 🎯 Overview

Phase 2 expands the library system with **database relationships** - enabling students to borrow books and reserve them. This phase teaches you:

- **One-to-Many Relationships** (@OneToMany / @ManyToOne)
- **Entity Associations** (Loan & Reservation entities linking Books & Students)
- **Service Business Logic** (validation, status tracking)
- **Transaction Management** (@Transactional)
- **RESTful Operations** (15+ new endpoints)
- **Interactive UI Dashboard** (manage all entities visually)

## 📊 New Entities

### Loan Entity
Represents when a student borrows a book.

```java
@Entity
@Table(name = "loans")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    
    private LocalDateTime loanDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    private Boolean isReturned = false;
}
```

**Key Features:**
- Tracks borrow date, due date, and return date
- Links Student → Books via @ManyToOne
- Prevents duplicate active loans

### Reservation Entity
Represents when a student waits for an unavailable book.

```java
@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    
    private LocalDateTime reservationDate;
    private Boolean isCancelled = false;
    private Integer queuePosition;
}
```

**Key Features:**
- Queue position tracking (who gets the book first)
- Auto-update queue when reservation cancelled
- Prevents duplicate reservations

## 🔗 Relationships Explained

### One-to-Many: Book → Loans
```
One Book can have Many Loans
|
└─ Book A has 3 loans (Student 1, Student 2, Student 3)
```

```java
// In Book.java
@OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
private List<Loan> loans;
```

**Why cascade=ALL?** 
- When you delete a book, all its loans are deleted too
- Prevents orphaned loan records in database

### One-to-Many: Student → Loans
```
One Student can have Many Loans
|
└─ Student A has 2 active loans (Book 1, Book 2)
```

```java
// In Student.java
@OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
private List<Loan> loans;
```

**mappedBy="student"?**
- Tells JPA that Loan class owns this relationship
- No extra column in student table (avoids duplication)

## 📋 API Endpoints - Phase 2

### Loan Endpoints (11 total)
| Method | Endpoint | Purpose |
|--------|----------|---------|
| POST | `/api/loans?studentId=1&bookId=1&dueDate=...` | Create new loan |
| GET | `/api/loans` | Get all loans |
| GET | `/api/loans/{id}` | Get loan by ID |
| GET | `/api/loans/student/{studentId}` | Get all loans for a student |
| GET | `/api/loans/student/{studentId}/active` | Get active loans (not returned) |
| GET | `/api/loans/book/{bookId}` | Get all loans for a book |
| GET | `/api/loans/active` | Get all active loans system-wide |
| GET | `/api/loans/overdue` | Get overdue loans (due date passed) |
| PUT | `/api/loans/{id}/return` | Return a book |
| DELETE | `/api/loans/{id}` | Delete loan record |
| GET | `/api/loans/count` | Total loans |

### Reservation Endpoints (9 total)
| Method | Endpoint | Purpose |
|--------|----------|---------|
| POST | `/api/reservations?studentId=1&bookId=1` | Create reservation |
| GET | `/api/reservations` | Get all reservations |
| GET | `/api/reservations/{id}` | Get by ID |
| GET | `/api/reservations/student/{studentId}` | Get student's reservations |
| GET | `/api/reservations/book/{bookId}` | Get book's reservations |
| GET | `/api/reservations/book/{bookId}/active` | Get active queue |
| GET | `/api/reservations/active` | Get all active reservations |
| PUT | `/api/reservations/{id}/cancel` | Cancel reservation |
| GET | `/api/reservations/count` | Total reservations |

## 🚀 Using the Dashboard UI

### Access the UI
```
https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/index.html
```

### Tabs Available:
1. **Dashboard** - System statistics and overview
2. **Books** - Add and view all books
3. **Students** - Add and view all students
4. **Loans** - Create loans and track borrowing
5. **Reservations** - Manage book reservations

### Example Workflow:
1. Go to **Books** tab → Add "Clean Code" by Robert C. Martin
2. Go to **Students** tab → Add student "Raj Kumar"
3. Go to **Loans** tab → Select Raj and Clean Code → "Create Loan"
4. See loan appear in dashboard with 14-day due date
5. When returned, click "Return" button

## 💡 Business Logic Examples

### Creating a Loan (Service Layer)
```java
@Transactional
public Loan createLoan(Long studentId, Long bookId, LocalDateTime dueDate) {
    // 1. Find student (throw if not found)
    Student student = studentRepository.findById(studentId)
        .orElseThrow(() -> new IllegalArgumentException("Student not found"));
    
    // 2. Find book (throw if not found)
    Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new IllegalArgumentException("Book not found"));
    
    // 3. Validate book is available
    if (!book.getAvailable()) {
        throw new IllegalArgumentException("Book not available");
    }
    
    // 4. Check for duplicate active loans
    Optional<Loan> existingLoan = loanRepository
        .findByStudentAndBookAndIsReturnedFalse(student, book);
    if (existingLoan.isPresent()) {
        throw new IllegalArgumentException("Already borrowed");
    }
    
    // 5. Create loan and update book availability
    Loan loan = new Loan(student, book, dueDate);
    book.setAvailable(false);  // Mark as unavailable
    bookRepository.save(book);
    return loanRepository.save(loan);
}
```

**@Transactional Magic:**
- Ensures all operations (loan save + book update) succeed together
- If any operation fails, both are rolled back
- Prevents data inconsistency

### Returning a Book
```java
@Transactional
public Loan returnBook(Long loanId) {
    Loan loan = loanRepository.findById(loanId)
        .orElseThrow(() -> new IllegalArgumentException("Loan not found"));
    
    if (loan.getIsReturned()) {
        throw new IllegalArgumentException("Already returned");
    }
    
    // Mark as returned
    loan.setIsReturned(true);
    loan.setReturnDate(LocalDateTime.now());
    
    // Make book available again
    Book book = loan.getBook();
    book.setAvailable(true);
    bookRepository.save(book);
    
    return loanRepository.save(loan);
}
```

## 🧪 Testing Phase 2

### Test 1: Create a Loan
```bash
# Create book first
curl -X POST https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/books \
  -H "Content-Type: application/json" \
  -d '{"title":"Spring in Action","author":"Craig Walls","isbn":"978-1617294945"}'

# Create student first
curl -X POST https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/students \
  -H "Content-Type: application/json" \
  -d '{"name":"Sarah","email":"sarah@example.com","rollNumber":"CS2024001"}'

# Create loan (use actual IDs)
curl -X POST "https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/loans?studentId=1&bookId=1"
```

### Test 2: Check Active Loans
```bash
curl https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/loans/active | jq .
```

### Test 3: Return a Book
```bash
curl -X PUT https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/loans/1/return
```

### Test 4: Create Reservation
```bash
curl -X POST "https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/reservations?studentId=1&bookId=1"
```

## 📁 Project Structure - Phase 2

```
library-management-system/
└── library-api/
    ├── src/main/java/com/example/library/
    │   ├── entity/
    │   │   ├── Book.java (updated with @OneToMany loans, reservations)
    │   │   ├── Student.java (updated with @OneToMany loans, reservations)
    │   │   ├── Loan.java (NEW - Phase 2)
    │   │   └── Reservation.java (NEW - Phase 2)
    │   ├── repository/
    │   │   ├── LoanRepository.java (NEW - Phase 2)
    │   │   └── ReservationRepository.java (NEW - Phase 2)
    │   ├── service/
    │   │   ├── LoanService.java (NEW - Phase 2)
    │   │   └── ReservationService.java (NEW - Phase 2)
    │   └── controller/
    │       ├── LoanController.java (NEW - Phase 2)
    │       └── ReservationController.java (NEW - Phase 2)
    └── src/main/resources/
        └── static/
            └── index.html (NEW - Interactive Dashboard UI)
```

## 🎓 Key Learning Concepts

### 1. Entity Relationships
- **@ManyToOne** - Many loans belong to one book
- **@OneToMany** - One book has many loans
- **mappedBy** - Specifies owning side of relationship
- **cascade** - What happens when parent is deleted

### 2. Business Logic Validation
```java
// Always validate in service layer (not controller)
if (!book.getAvailable()) {
    throw new IllegalArgumentException("Book not available");
}
```

### 3. Avoiding Duplicate Records
```java
// Check before creating new record
Optional<Loan> existing = repository.findByStudentAndBookAndIsReturnedFalse(student, book);
if (existing.isPresent()) {
    throw new IllegalArgumentException("Already borrowed");
}
```

### 4. Transaction Management
```java
@Transactional  // All or nothing - ensures consistency
public Loan returnBook(Long loanId) {
    // Multiple operations treated as single unit
    loan.setIsReturned(true);
    book.setAvailable(true);
    // Both saved together or both rolled back
}
```

### 5. Status Tracking
```java
// Track state with flags and dates
loan.isReturned = false;  // Boolean status
loan.returnDate = null;   // Date tracking
loan.dueDate = LocalDateTime.now().plusDays(14);  // Business rules
```

## 🔄 Workflow: Borrow → Due → Return

```
1. Student wants to borrow book
   └─ Check if book available (Book.available = true)
   
2. Create Loan record
   └─ Book.available = false (mark as borrowed)
   └─ Loan.loanDate = today
   └─ Loan.dueDate = today + 14 days
   └─ Loan.isReturned = false
   
3. Book is borrowed (in Student's hands)
   └─ Can create Reservation if needed
   └─ Check for overdue (dueDate < today)
   
4. Student returns book
   └─ Update Loan.isReturned = true
   └─ Loan.returnDate = today
   └─ Book.available = true (mark as available)
   └─ Give to next student in queue (reservation queue position #1)
```

## ✅ What's Next - Phase 3?

- **Advanced Queries** - Search books by filters, pagination
- **User Authentication** - Login/logout with JWT
- **Penalties & Fines** - Calculate overdue penalties
- **Email Notifications** - Remind about due dates
- **Database Caching** - Improve query performance

## 📚 Resources

- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Hibernate ORM Relationships](https://docs.jboss.org/hibernate/orm/6.4/userguide/html_single/)
- [REST API Design Best Practices](https://restfulapi.net/)
- [Transaction Management](https://spring.io/guides/gs/managing-transactions-with-spring/)
