# 📚 Smart Library Management System
## Spring Boot Learning Project (Phases 1-6)

### 🎯 Goal
Build ONE expandable Spring Boot application to learn backend development from beginner to job-ready.

**Current Status:** ✅ **PHASE 1 COMPLETE** - Core CRUD Operations

---

## 📋 PHASE 1: Core CRUD (COMPLETE)

### What You'll Learn
✅ Spring Boot fundamentals  
✅ REST API design  
✅ JPA/Hibernate basics  
✅ Clean architecture (Controller → Service → Repository)  
✅ CRUD operations  
✅ H2 in-memory database  
✅ Dependency injection  

### Project Structure
```
library-api/
├── controller/       (HTTP endpoints)
├── service/          (Business logic)
├── repository/       (Database access)
├── entity/           (Database models)
└── resources/        (Configuration)
```

### Key Technologies
- **Java 17**
- **Spring Boot 3.2.1**
- **Spring Data JPA** (Hibernate)
- **H2 Database** (in-memory, embedded)
- **Maven** (dependency management)

---

## 🚀 GETTING STARTED

### Prerequisites
- Java 17+
- Maven 3.8+
- Postman (for API testing)

### Run the Application

```bash
cd library-api
mvn spring-boot:run
```

**Server will start on:** https://studious-lamp-6654w7vjgxv3gp-8080.app.github.dev/api

---

## 📝 API ENDPOINTS (Phase 1)

### Books Management

| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/books` | Get all books |
| GET | `/books/{id}` | Get book by ID |
| POST | `/books` | Create new book |
| PUT | `/books/{id}` | Update book |
| DELETE | `/books/{id}` | Delete book |
| GET | `/books/search/title?q=` | Search by title |
| GET | `/books/search/author?q=` | Search by author |
| GET | `/books/available` | Get available books |
| GET | `/books/borrowed` | Get borrowed books |
| GET | `/books/count` | Get total count |

### Students Management

| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/students` | Get all students |
| GET | `/students/{id}` | Get student by ID |
| POST | `/students` | Register new student |
| PUT | `/students/{id}` | Update student |
| DELETE | `/students/{id}` | Remove student |
| GET | `/students/search/email?q=` | Find by email |
| GET | `/students/search/rollnumber?q=` | Find by roll number |
| GET | `/students/active` | Get active students |
| GET | `/students/count` | Get total count |

---

## 📖 DETAILED LEARNING GUIDE

See [PHASE_1_GUIDE.md](./PHASE_1_GUIDE.md) for:
- Comprehensive concept explanations
- Why each annotation is used
- Common beginner mistakes
- Step-by-step testing with Postman
- Best practices

---

## 🧪 TESTING WITH POSTMAN

### Quick Start

1. **Import Base URL:** `https://studious-lamp-6654w7vjgxv3gp-8080.app.github.dev/api`

2. **Create a Book:**
```json
POST /books
{
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "isbn": "978-0132350884",
  "available": true
}
```

3. **Get All Books:**
```
GET /books
```

4. **Search:**
```
GET /books/search/title?q=Clean
```

See [PHASE_1_GUIDE.md](./PHASE_1_GUIDE.md#-testing-with-postman) for complete test scenarios.

---

## 🗄️ H2 DATABASE CONSOLE

Access the embedded H2 database GUI:

**URL:** https://studious-lamp-6654w7vjgxv3gp-8080.app.github.dev/h2-console

**Credentials:**
- JDBC URL: `jdbc:h2:mem:librarydb`
- Username: `sa`
- Password: (leave empty)

View and query tables directly!

---

## 🔑 KEY CONCEPTS (Phase 1)

### Three-Layer Architecture
```
┌─────────────────────────┐
│   CONTROLLER Layer      │
│ (HTTP Requests/Response)│
└────────────┬────────────┘
             ↓
┌─────────────────────────┐
│   SERVICE Layer         │
│ (Business Logic)        │
└────────────┬────────────┘
             ↓
┌─────────────────────────┐
│  REPOSITORY Layer       │
│ (Database Access)       │
└────────────┬────────────┘
             ↓
┌─────────────────────────┐
│    H2 DATABASE          │
└─────────────────────────┘
```

### REST Conventions
- **Resource:** `/api/books` (plural nouns)
- **Operations:** GET, POST, PUT, DELETE
- **Status Codes:** 200 (OK), 201 (Created), 204 (No Content), 404 (Not Found)
- **Format:** JSON

### JPA Entity Annotations
```java
@Entity              // Maps to database table
@Table(name = "books")
@Id                  // Primary key
@GeneratedValue      // Auto-increment
@Column              // Column constraints
@PrePersist          // Before save
```

---

## 📚 WHAT'S NEXT? (Phase 2+)

### Phase 2: Database Relationships
- One-to-Many (Student borrows many Books)
- Many-to-One (Book borrowed by Student)
- Pagination & sorting
- JPQL queries

### Phase 3: Validation & Exception Handling
- Input validation
- Custom exceptions
- Global error handling
- HTTP status codes

### Phase 4: Security
- User authentication
- Password hashing
- JWT tokens
- Role-based access

### Phase 5: Advanced APIs
- DTO pattern
- API response structure
- Complex filtering

### Phase 6: Production Ready
- MySQL integration
- Logging
- Swagger/OpenAPI
- Docker

---

## ⚡ QUICK REFERENCE

### Common Maven Commands

```bash
# Build project
mvn clean install

# Run application
mvn spring-boot:run

# Run tests
mvn test

# Create package
mvn package

# Clean build files
mvn clean
```

### Spring Boot Annotations

| Annotation | Purpose |
|-----------|---------|
| `@SpringBootApplication` | Entry point |
| `@RestController` | HTTP endpoint |
| `@Service` | Business logic |
| `@Repository` | Data access |
| `@Entity` | Database model |
| `@Autowired` | Dependency injection |
| `@RequestMapping` | Route mapping |
| `@GetMapping`, `@PostMapping` | HTTP methods |

---

## 📞 COMMON ISSUES & FIXES

### Issue: "Cannot find symbol" for getters/setters
**Fix:** Ensure Lombok is installed and JPA annotations are on fields

### Issue: "Table not found" error
**Fix:** H2 database starts empty. Try `spring.jpa.hibernate.ddl-auto=create` in `application.properties`

### Issue: Port 8080 already in use
**Fix:** Change port in `application.properties`:
```properties
server.port=8081
```

### Issue: "No qualifying bean" error
**Fix:** Ensure class has `@Service`, `@Repository`, or `@RestController` annotation

---

## 🎓 LEARNING OBJECTIVES

By the end of Phase 1, you should understand:

✅ What Spring Boot is and why use it  
✅ How REST APIs work  
✅ JPA/Hibernate ORM basics  
✅ Clean architecture principles  
✅ Dependency injection  
✅ CRUD operations  
✅ HTTP status codes  
✅ Database modeling  
✅ Testing REST APIs  

---

## 📖 RESOURCES

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA Guide](https://spring.io/projects/spring-data-jpa)
- [REST Best Practices](https://restfulapi.net/)
- [Baeldung Tutorials](https://www.baeldung.com/)

---

## ✅ NEXT STEPS

1. **Run the application** and test endpoints with Postman
2. **Read PHASE_1_GUIDE.md** for detailed explanations
3. **Experiment:** Modify entities, add new endpoints
4. **Ask questions** before moving to Phase 2

**Once you're confident with Phase 1, request Phase 2: Database Relationships!**

---

**Happy Learning! 🚀**
