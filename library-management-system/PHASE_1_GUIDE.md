# 📚 Phase 1: Core CRUD - Complete Guide

## 🎯 Learning Objectives
By the end of Phase 1, you will understand:
- ✅ What Spring Boot is and how it works
- ✅ What REST APIs are and how to build them
- ✅ What JPA/Hibernate is and how it maps objects to databases
- ✅ Clean Architecture with Controller → Service → Repository layers
- ✅ How to perform CRUD operations
- ✅ How to test APIs using Postman

---

## 📚 CONCEPT EXPLANATIONS

### 1. What is Spring Boot?

**Traditional Spring Framework:**
```
❌ Manual XML configuration
❌ Complex setup
❌ Lots of boilerplate code
❌ Steep learning curve
```

**Spring Boot:**
```
✅ Auto-configuration
✅ Minimal setup
✅ Convention over configuration
✅ Embedded server (Tomcat)
✅ Production-ready in minutes
```

**Spring Boot provides:**
- Web server (Tomcat)
- Database configuration
- Dependency injection
- Auto-wiring components
- Built-in testing support

**In our project:**
```
LibraryApplication (Main class)
    ↓
Spring Boot starts Tomcat server on port 8080
    ↓
Scans packages for @SpringBootApplication
    ↓
Initializes @Components, @Services, @Repositories
    ↓
API ready to receive HTTP requests
```

---

### 2. What is REST?

**REST = Representational State Transfer**

**Key Principles:**
- Resources are identified by URLs (nouns, plural)
- Operations use HTTP methods (verbs)
- State is represented by HTTP status codes
- Data usually in JSON format

**Standard REST Conventions:**

```
RESOURCE: /api/books (plural)
OPERATION: HTTP Method

GET /api/books              → Fetch all books (200 OK)
GET /api/books/1            → Fetch book with ID=1 (200 OK or 404 NOT FOUND)
POST /api/books             → Create new book (201 CREATED)
PUT /api/books/1            → Update book with ID=1 (200 OK)
DELETE /api/books/1         → Delete book with ID=1 (204 NO CONTENT)
```

**HTTP Status Codes:**
```
200 OK              → Success, response body included
201 CREATED         → Resource successfully created
204 NO CONTENT      → Success, no response body
400 BAD REQUEST     → Invalid input data
404 NOT FOUND       → Resource doesn't exist
500 INTERNAL ERROR  → Server error
```

---

### 3. What is JPA/Hibernate?

**Problem without JPA:**
```java
// ❌ Without JPA - Manual SQL everywhere
String sql = "INSERT INTO books (title, author) VALUES (?, ?)";
PreparedStatement ps = connection.prepareStatement(sql);
ps.setString(1, "Clean Code");
ps.setString(2, "Robert C. Martin");
ps.executeUpdate();
```

**With JPA:**
```java
// ✅ With JPA - Object-oriented
Book book = new Book("Clean Code", "Robert C. Martin");
bookRepository.save(book);  // That's it!
```

**JPA Benefits:**
- Write code in Java objects, not SQL
- Database changes don't affect code
- Automatic table creation
- Type-safe queries
- Built-in caching

**ORM = Object-Relational Mapping**
```
Java Class ↔ Database Table
Field      ↔ Column
Object     ↔ Row
```

**How it works:**
```
@Entity
public class Book {
    @Id
    @GeneratedValue
    Long id;           ↔ CREATE TABLE books (
                           id BIGINT PRIMARY KEY AUTO_INCREMENT,
    @Column
    String title;          title VARCHAR(200) NOT NULL,
    
    @Column
    String author;         author VARCHAR(100) NOT NULL,
                       );
}
```

**Key Annotations:**
```java
@Entity         // This class maps to a database table
@Table          // Specify table name and constraints
@Id             // Primary key
@GeneratedValue // Auto-increment ID
@Column         // Column constraints (nullable, unique, length)
@PrePersist     // Execute before saving (timestamps)
```

---

### 4. Clean Architecture Layers

**Three-Layer Architecture:**

```
┌─────────────────────────────────────┐
│  CONTROLLER (HTTP Layer)            │
│  BookController.java                │
│  - Receives HTTP requests           │
│  - Validates request format         │
│  - Returns JSON responses           │
│  - Sets HTTP status codes           │
└──────────────┬──────────────────────┘
               ↓
┌──────────────────────────────────────┐
│  SERVICE (Business Logic Layer)      │
│  BookService.java                    │
│  - Business rules & validation       │
│  - Orchestrates repositories         │
│  - Transactions                      │
│  - Complex operations                │
└──────────────┬──────────────────────┘
               ↓
┌──────────────────────────────────────┐
│  REPOSITORY (Data Access Layer)      │
│  BookRepository.java                 │
│  - Database queries                  │
│  - CRUD operations                   │
│  - Query methods                     │
└──────────────┬──────────────────────┘
               ↓
┌──────────────────────────────────────┐
│  DATABASE (H2 In-Memory)             │
│  - Stores data                       │
│  - Executes queries                  │
└──────────────────────────────────────┘
```

**Why three layers?**
- **Separation of Concerns:** Each layer has one responsibility
- **Testability:** Easy to unit test each layer independently
- **Reusability:** Same service can be used by multiple controllers
- **Maintainability:** Changes in database don't affect controllers
- **Scalability:** Easy to add new features

---

## 🏗️ PROJECT STRUCTURE (CREATED)

```
library-api/
│
├── pom.xml                          (Maven configuration)
├── src/
│   ├── main/
│   │   ├── java/com/example/library/
│   │   │   ├── LibraryApplication.java        (Main class)
│   │   │   ├── controller/
│   │   │   │   ├── BookController.java        (HTTP endpoints for books)
│   │   │   │   └── StudentController.java     (HTTP endpoints for students)
│   │   │   ├── service/
│   │   │   │   ├── BookService.java           (Book business logic)
│   │   │   │   └── StudentService.java        (Student business logic)
│   │   │   ├── repository/
│   │   │   │   ├── BookRepository.java        (Book data access)
│   │   │   │   └── StudentRepository.java     (Student data access)
│   │   │   └── entity/
│   │   │       ├── Book.java                  (Book model/table)
│   │   │       └── Student.java               (Student model/table)
│   │   └── resources/
│   │       └── application.properties         (Configuration)
│   └── test/
│       └── java/...                           (Unit tests)
└── target/                                     (Compiled files)
```

---

## 🔑 Key Annotations Explained

### Entity Annotations

```java
@Entity                 // Maps this class to a database table
@Table(name = "books")  // Specifies table name (optional)

// Inside class:
@Id                                    // Primary key
@GeneratedValue(strategy = IDENTITY)   // Auto-increment
@Column(name = "title",                // Column name
        nullable = false,              // NOT NULL
        length = 200)                  // VARCHAR(200)
private String title;
```

### Repository Annotations

```java
@Repository            // Marks as data access component
                       // Enables exception translation

// Methods follow Spring Data naming convention:
findBy{FieldName}      // SELECT * WHERE field = ?
findAll()              // SELECT * (provided by JpaRepository)
save()                 // INSERT or UPDATE (provided)
deleteById()           // DELETE WHERE id = ? (provided)
```

### Service Annotations

```java
@Service               // Marks as business logic component
@RequiredArgsConstructor  // Lombok generates constructor with required fields
                          // Used for dependency injection
```

### Controller Annotations

```java
@RestController                    // Combines @Controller + @ResponseBody
@RequestMapping("/books")          // Base path for all endpoints

// Method level:
@GetMapping            // HTTP GET request
@PostMapping           // HTTP POST request
@PutMapping            // HTTP PUT request
@DeleteMapping         // HTTP DELETE request

// Parameter level:
@PathVariable Long id  // Extract from URL: /api/books/{id}
@RequestBody Book book // Convert JSON to object
@RequestParam String q // Extract query param: ?q=...
```

---

## 📝 COMMON BEGINNER MISTAKES & FIXES

### ❌ Mistake 1: Putting all code in Controller

```java
// ❌ BAD
@RestController
public class BookController {
    @PostMapping
    public Book createBook(@RequestBody Book book) {
        // Database query here ❌
        // Business logic here ❌
        // Everything in controller ❌
    }
}

// ✅ GOOD
@RestController
public class BookController {
    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookService.createBook(book);  // ✅ Delegate to service
    }
}
```

**Why?** Controllers become too complex, hard to test, can't reuse logic.

---

### ❌ Mistake 2: Forgetting @Transactional or not understanding lazy loading

```java
// ❌ BAD - Will cause LazyInitializationException in Phase 2
@GetMapping("/{id}")
public Book getBook(@PathVariable Long id) {
    return bookRepository.findById(id).get();
    // If accessing related entities → Error!
}

// ✅ GOOD - We'll add this in Phase 2
@Transactional  // Keeps session open
@GetMapping("/{id}")
public Book getBook(@PathVariable Long id) {
    return bookRepository.findById(id).get();
}
```

---

### ❌ Mistake 3: Not validating input

```java
// ❌ BAD
public Book createBook(Book book) {
    return bookRepository.save(book);  // What if title is null?
}

// ✅ GOOD
public Book createBook(Book book) {
    if (book.getTitle() == null || book.getTitle().isBlank()) {
        throw new IllegalArgumentException("Title required");
    }
    return bookRepository.save(book);
}
```

---

### ❌ Mistake 4: Wrong HTTP status codes

```java
// ❌ BAD
@PostMapping
public ResponseEntity<Book> create(@RequestBody Book book) {
    return new ResponseEntity<>(bookService.save(book), HttpStatus.OK); // ❌ Should be CREATED
}

// ✅ GOOD
@PostMapping
public ResponseEntity<Book> create(@RequestBody Book book) {
    return new ResponseEntity<>(bookService.save(book), HttpStatus.CREATED); // ✅
}
```

---

### ❌ Mistake 5: Not handling Optional properly

```java
// ❌ BAD
@GetMapping("/{id}")
public Book getBook(@PathVariable Long id) {
    return bookRepository.findById(id).get();  // ❌ Throws NoSuchElementException
}

// ✅ GOOD
@GetMapping("/{id}")
public ResponseEntity<Book> getBook(@PathVariable Long id) {
    Optional<Book> book = bookRepository.findById(id);
    if (book.isPresent()) {
        return new ResponseEntity<>(book.get(), HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // ✅ Proper error handling
}
```

---

## 🧪 TESTING WITH POSTMAN

### Setup Postman

1. Download: https://www.postman.com/downloads/
2. Create a new collection: "Library API"
3. Set base URL: http://localhost:8080/api

### Test Cases

#### 1. CREATE BOOK
```
POST http://localhost:8080/api/books
Content-Type: application/json

{
    "title": "Clean Code",
    "author": "Robert C. Martin",
    "isbn": "978-0132350884",
    "available": true
}

Expected Response: 201 CREATED
{
    "id": 1,
    "title": "Clean Code",
    "author": "Robert C. Martin",
    "isbn": "978-0132350884",
    "available": true,
    "createdAt": 1674856234567
}
```

#### 2. GET ALL BOOKS
```
GET http://localhost:8080/api/books
Content-Type: application/json

Expected Response: 200 OK
[
    {
        "id": 1,
        "title": "Clean Code",
        ...
    }
]
```

#### 3. GET BOOK BY ID
```
GET http://localhost:8080/api/books/1

Expected Response: 200 OK (or 404 if not found)
{
    "id": 1,
    "title": "Clean Code",
    ...
}
```

#### 4. UPDATE BOOK
```
PUT http://localhost:8080/api/books/1
Content-Type: application/json

{
    "title": "Clean Code (Updated Edition)",
    "available": false
}

Expected Response: 200 OK
{
    "id": 1,
    "title": "Clean Code (Updated Edition)",
    "available": false,
    ...
}
```

#### 5. DELETE BOOK
```
DELETE http://localhost:8080/api/books/1

Expected Response: 204 NO CONTENT (empty body)
```

#### 6. SEARCH BY TITLE
```
GET http://localhost:8080/api/books/search/title?q=Clean

Expected Response: 200 OK
[
    {
        "id": 1,
        "title": "Clean Code",
        ...
    }
]
```

#### 7. GET AVAILABLE BOOKS
```
GET http://localhost:8080/api/books/available

Expected Response: 200 OK
[...]
```

#### 8. CREATE STUDENT
```
POST http://localhost:8080/api/students
Content-Type: application/json

{
    "name": "John Doe",
    "email": "john@example.com",
    "rollNumber": "CS001",
    "active": true
}

Expected Response: 201 CREATED
{
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "rollNumber": "CS001",
    "active": true,
    "createdAt": 1674856234567
}
```

#### 9. H2 DATABASE CONSOLE
```
URL: http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:librarydb
User: sa
Password: (leave empty)

View and query tables directly!
```

---

## ✅ WHAT YOU LEARNED IN PHASE 1

1. **Spring Boot Basics**
   - What is Spring Boot and why use it?
   - How to set up a Spring Boot project
   - Auto-configuration and convention-over-configuration

2. **REST API Design**
   - HTTP methods (GET, POST, PUT, DELETE)
   - HTTP status codes (200, 201, 204, 400, 404)
   - Resource-oriented design

3. **JPA & Hibernate**
   - Entity mapping (@Entity, @Column, @Id)
   - Primary keys and auto-increment
   - Data persistence

4. **Clean Architecture**
   - Three-layer pattern (Controller → Service → Repository)
   - Separation of concerns
   - Why each layer matters

5. **Dependency Injection**
   - @Autowired and constructor injection
   - Spring's component scanning
   - How Spring manages beans

6. **CRUD Operations**
   - CREATE (POST)
   - READ (GET)
   - UPDATE (PUT)
   - DELETE (DELETE)

7. **Testing APIs**
   - Using Postman for manual testing
   - Understanding request/response format
   - Testing different HTTP methods

---

## 🎯 NEXT PHASE (Phase 2)

In Phase 2, you'll learn:
- **Entity Relationships** (One-to-Many, Many-to-One)
- **Student borrows Book** relationship
- **Pagination & Sorting**
- **JPQL queries**
- **Fetch types** (Lazy vs Eager)

---

## 📖 KEY TERMS GLOSSARY

| Term | Meaning |
|------|---------|
| **Spring Boot** | Framework that simplifies Spring development |
| **REST** | Architecture style for building web APIs |
| **JPA** | Standard for Object-Relational Mapping |
| **Hibernate** | JPA implementation (database driver) |
| **Entity** | Java class that maps to database table |
| **Repository** | Data access object for database operations |
| **Service** | Business logic layer |
| **Controller** | HTTP request handler |
| **DTO** | Data Transfer Object (Phase 5) |
| **Dependency Injection** | Spring provides dependencies automatically |
| **Bean** | Object managed by Spring container |
| **Optional** | Wrapper class to handle null values safely |

---

## 🔗 USEFUL RESOURCES

- Spring Boot Docs: https://spring.io/projects/spring-boot
- Spring Data JPA: https://spring.io/projects/spring-data-jpa
- Baeldung Tutorials: https://www.baeldung.com/
- REST Best Practices: https://restfulapi.net/

---

**Now you're ready to test Phase 1! 🚀**
