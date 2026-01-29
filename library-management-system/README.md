# 📚 Smart Library Management System
## Spring Boot Learning Project (Phases 1-6)

### 🎯 Goal
Build ONE expandable Spring Boot application to learn backend development from beginner to job-ready.

**Current Status:** ✅ **PHASE 3 COMPLETE** - Advanced Queries & Pagination | 64+ REST Endpoints

---

## 📊 Project Progress

| Phase | Status | Features | Endpoints |
|-------|--------|----------|-----------|
| 1: Core CRUD | ✅ Complete | Books, Students, Basic Search | 20 |
| 2: Relationships | ✅ Complete | Loans, Reservations, Dashboard UI | 20 |
| 3: Advanced Queries | ✅ Complete | Pagination, Full-Text Search, Sorting | 24 |
| 4: Authentication | ⏳ Planned | Login, JWT, Role-Based Access | 5-10 |
| 5: Notifications | ⏳ Planned | Email Alerts, Fine Calculations | 8-12 |
| 6: Deployment | ⏳ Planned | Docker, CI/CD, Cloud Deployment | - |

**Total Endpoints:** 64+

---

## ✨ Phase 3 Features (NEW!)

### Advanced Search
```bash
# Search books by title or author
curl "https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/books/search/query?query=spring"

# Search students by name or email
curl "https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/students/search/query?query=john"

# Pattern matching on roll numbers
curl "https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/students/search/rollpattern?pattern=CS"
```

### Pagination
```bash
# Paginated book search
curl "https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/books/search/paginated?query=java&page=0&size=10"

Response includes: content, pageNumber, pageSize, totalElements, totalPages, hasNext, hasPrevious
```

### Sorting
```bash
# Sort books by title (A-Z)
curl "https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/books/sorted/title?asc=true&page=0&size=10"

# Sort students by name (Z-A)
curl "https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/students/sorted/name?asc=false&page=0&size=10"
```

### Advanced Loan Queries
```bash
# Get loans due between dates
curl "https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/loans/due/between?startDate=2026-01-28T00:00:00&endDate=2026-02-28T23:59:59"

# Get student's active loan count
curl "https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/loans/student/1/count-active"

# Get recently returned loans
curl "https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/loans/recently-returned"
```

---

## 🚀 QUICK START

### Prerequisites
- Java 17+
- Maven 3.8+

### Run the Application

```bash
cd /workspaces/codespaces-blank/library-management-system/library-api

# Build
mvn clean package -DskipTests

# Run
mvn spring-boot:run
```

**Server will start on:** https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev (API) and https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/index.html (Dashboard)

---

## � COMPREHENSIVE GUIDES

### Phase 1: Core CRUD
- **Guide:** [PHASE_1_GUIDE.md](./PHASE_1_GUIDE.md) - Detailed tutorial
- **Quick Start:** [PHASE_1_QUICK_START.md](./PHASE_1_QUICK_START.md) - Quick reference
- **Topics:** REST APIs, CRUD operations, JPA basics, repositories

### Phase 2: Database Relationships  
- **Guide:** [PHASE_2_GUIDE.md](./PHASE_2_GUIDE.md) - Detailed tutorial
- **Quick Start:** [PHASE_2_QUICK_START.md](./PHASE_2_QUICK_START.md) - Quick reference
- **Topics:** Entity relationships, One-to-Many, cascading, interactive UI

### Phase 3: Advanced Queries & Pagination (🆕 COMPLETE)
- **Guide:** [PHASE_3_GUIDE.md](./PHASE_3_GUIDE.md) - Detailed tutorial
- **Quick Start:** [PHASE_3_QUICK_START.md](./PHASE_3_QUICK_START.md) - Quick reference with test cases
- **Completion:** [PHASE_3_COMPLETION.md](./PHASE_3_COMPLETION.md) - Detailed completion report
- **Topics:** JPQL queries, custom repositories, pagination, sorting, filtering

---

## 📝 API ENDPOINTS (All Phases)

### Books (16 endpoints)
**CRUD (10):** Create, Read, Update, Delete, Get All, Search by title/author/ISBN, Filter available/unavailable  
**Phase 3 (6):** Full-text search, multi-field search, paginated results, sorted results

### Students (17 endpoints)
**CRUD (10):** Create, Read, Update, Delete, Get All, Search by name/email/roll, Filter active/inactive  
**Phase 3 (7):** Full-text search, pattern matching, paginated results, sorted results, count active

### Loans (22 endpoints)
**Basic (11):** Create, Read, Update, Delete, Get by student/book, Filter active/overdue/returned  
**Phase 3 (11):** Sort by due date, date range queries, recently returned, paginated results, count per student

### Reservations (9 endpoints)
**CRUD (9):** Create, Read, Update, Delete, Get by student/book, Filter active/cancelled

**Total: 64+ Endpoints**

---

## 🏗️ ARCHITECTURE OVERVIEW

```
REST Client (curl, browser, Postman)
        ↓
┌───────────────────────┐
│ Spring Boot Server    │
│ (Port 8000)          │
├───────────────────────┤
│ Controllers (REST)    │
│ - BookController      │
│ - StudentController   │
│ - LoanController      │
│ - ReservationController
├───────────────────────┤
│ Services (Business)   │
│ - BookService         │
│ - StudentService      │
│ - LoanService         │
│ - ReservationService  │
│ - PaginationService ✨ (Phase 3)
├───────────────────────┤
│ Repositories (Data)   │
│ - BookRepository      │
│ - StudentRepository   │
│ - LoanRepository      │
│ - ReservationRepository
│ With 17 Custom @Query Methods ✨
├───────────────────────┤
│ JPQL Queries          │
│ (Database Search)     │
├───────────────────────┤
│ H2 Database           │
│ (4 Tables)            │
└───────────────────────┘
```

---

## 🗄️ DATABASE SCHEMA

### Books Table
```
├── id (Long) - Primary Key
├── title (String) - Book title
├── author (String) - Author name
├── isbn (String) - ISBN code
├── available (Boolean) - In stock?
├── loans (One-to-Many) → Loan entities
└── reservations (One-to-Many) → Reservation entities
```

### Students Table
```
├── id (Long) - Primary Key
├── name (String) - Student name
├── email (String) - Email address
├── rollNumber (String) - Roll number
├── active (Boolean) - Active status
├── loans (One-to-Many) → Loan entities
└── reservations (One-to-Many) → Reservation entities
```

### Loans Table
```
├── id (Long) - Primary Key
├── student (Many-to-One) → Student
├── book (Many-to-One) → Book
├── loanDate (LocalDateTime) - Borrow date
├── dueDate (LocalDateTime) - Due date
├── returnDate (LocalDateTime) - Actual return date
└── isReturned (Boolean) - Returned status
```

### Reservations Table
```
├── id (Long) - Primary Key
├── student (Many-to-One) → Student
├── book (Many-to-One) → Book
├── reservationDate (LocalDateTime) - Reservation date
├── queuePosition (Integer) - Position in queue
└── cancelled (Boolean) - Cancellation status
```

---

## 🧪 TESTING PHASE 3 FEATURES

### Add Sample Data
```bash
# Add books
curl -X POST https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/books \
  -H "Content-Type: application/json" \
  -d '{"title":"Spring in Action","author":"Craig Walls","isbn":"123","available":true}'

# Add students
curl -X POST https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/students \
  -H "Content-Type: application/json" \
  -d '{"name":"Alice Johnson","email":"alice@uni.edu","rollNumber":"CS001","active":true}'
```

### Test Search
```bash
# Full-text book search
curl "https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/books/search/query?query=spring"

# Student pattern matching
curl "https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/students/search/rollpattern?pattern=CS"
```

### Test Pagination
```bash
# Paginated search with metadata
curl "https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/books/search/paginated?query=java&page=0&size=10"

# Response includes: content, pageNumber, pageSize, totalElements, totalPages, hasNext, hasPrevious
```

### Test Sorting
```bash
# Sort ascending
curl "https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/books/sorted/title?asc=true&page=0&size=10"

# Sort descending
curl "https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/books/sorted/title?asc=false&page=0&size=10"
```

### Test Advanced Loan Queries
```bash
# Date range queries
curl "https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/loans/due/between?startDate=2026-01-28T00:00:00&endDate=2026-02-28T23:59:59"

# Student statistics
curl "https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/loans/student/1/count-active"

# Recent activity
curl "https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/loans/recently-returned"
```

See [PHASE_3_QUICK_START.md](./PHASE_3_QUICK_START.md) for comprehensive test cases with expected responses.

---

## 🔧 KEY TECHNOLOGIES

```
Java 17+
├── Spring Boot 3.2.1
│   ├── Spring Web (REST APIs)
│   ├── Spring Data JPA (Database ORM)
│   └── Spring Context (Dependency Injection)
├── Hibernate 6.4.1 (ORM Framework)
├── H2 Database 2.2.224 (In-Memory Database)
├── Tomcat 10.1.17 (Embedded Web Server)
└── Maven 3.8+ (Build & Dependency Tool)
```

### Phase 3 Additions
- **JPQL** - Java Persistence Query Language
- **Custom @Query** - Annotations for complex queries
- **PageResponse<T>** - Generic pagination DTO
- **PaginationService** - Pagination utility helper
- **Generic Programming** - Reusable components with <T>

---

## 📈 PROJECT STATISTICS

| Metric | Phase 1 | Phase 2 | Phase 3 | Total |
|--------|---------|---------|---------|-------|
| Entities | 2 | 4 | 4 | 4 |
| Endpoints | 20 | 20 | 24 | 64+ |
| Repositories | 2 | 4 | 4 | 4 |
| Services | 2 | 4 | 5 | 5 |
| Controllers | 2 | 4 | 4 | 4 |
| Custom Queries | 0 | 0 | 17 | 17 |
| DTOs | 0 | 0 | 1 | 1 |
| Source Files | 8 | 16 | 30+ | 30+ |

**Build Status:** ✅ SUCCESS | **Compilation Errors:** 0 | **Coverage:** Manual + Curl tests

---

## 🎯 LEARNING OUTCOMES

### Phase 1: Core Skills
- REST API design and HTTP methods
- JPA/Hibernate basics
- Controller-Service-Repository pattern
- CRUD operations
- Basic search functionality

### Phase 2: Relationships & UI
- Entity relationships (@OneToMany, @ManyToOne)
- Cascading operations
- Complex object modeling
- Frontend integration
- Interactive dashboard

### Phase 3: Advanced Database Features
- JPQL custom queries
- Full-text search implementation
- Pagination design pattern
- Advanced filtering and sorting
- Aggregation queries (COUNT)
- Date range filtering
- Generic programming patterns

---

## 🚀 NEXT PHASES (Coming Soon)

### Phase 4: Authentication & Security
- User login/logout with JWT tokens
- Password encryption (bcrypt)
- Role-based access control (RBAC)
- Protected endpoints
- User registration

### Phase 5: Notifications & Penalties
- Email notifications for overdue books
- Fine calculation system
- Reminder emails
- Notification API

### Phase 6: Deployment & DevOps
- Docker containerization
- CI/CD pipeline (GitHub Actions)
- Cloud deployment (AWS/GCP/Azure)
- Production logging
- Performance monitoring

---

## 🎓 PROJECT STRUCTURE

```
library-management-system/
├── library-api/
│   ├── src/main/java/com/library/
│   │   ├── entity/               (4 JPA Entities)
│   │   │   ├── Book.java
│   │   │   ├── Student.java
│   │   │   ├── Loan.java
│   │   │   └── Reservation.java
│   │   ├── repository/           (4 Repositories)
│   │   │   ├── BookRepository.java
│   │   │   ├── StudentRepository.java
│   │   │   ├── LoanRepository.java
│   │   │   └── ReservationRepository.java
│   │   ├── service/              (5 Services)
│   │   │   ├── BookService.java
│   │   │   ├── StudentService.java
│   │   │   ├── LoanService.java
│   │   │   ├── ReservationService.java
│   │   │   └── PaginationService.java ✨ (Phase 3)
│   │   ├── controller/           (4 Controllers)
│   │   │   ├── BookController.java
│   │   │   ├── StudentController.java
│   │   │   ├── LoanController.java
│   │   │   └── ReservationController.java
│   │   ├── dto/                  (DTOs)
│   │   │   └── PageResponse.java ✨ (Phase 3)
│   │   └── LibraryApiApplication.java
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   └── (database config)
│   ├── src/main/static/
│   │   └── index.html            (Interactive Dashboard UI)
│   └── pom.xml
│
├── PHASE_1_GUIDE.md
├── PHASE_1_QUICK_START.md
├── PHASE_2_GUIDE.md
├── PHASE_2_QUICK_START.md
├── PHASE_3_GUIDE.md
├── PHASE_3_QUICK_START.md
├── PHASE_3_COMPLETION.md
└── README.md (this file)
```

---

## ⚡ QUICK COMMANDS

```bash
# Build and run
cd /workspaces/codespaces-blank/library-management-system/library-api
mvn clean package -DskipTests
mvn spring-boot:run

# Access application
# API: https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api
# Dashboard: https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev
# H2 Console: https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/h2-console

# Test endpoints
curl https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/books
curl "https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/books/search/query?query=java"
```

---

## 📞 SUPPORT & DOCUMENTATION

| Resource | Link |
|----------|------|
| Phase 1 Guide | [PHASE_1_GUIDE.md](./PHASE_1_GUIDE.md) |
| Phase 1 Quick Start | [PHASE_1_QUICK_START.md](./PHASE_1_QUICK_START.md) |
| Phase 2 Guide | [PHASE_2_GUIDE.md](./PHASE_2_GUIDE.md) |
| Phase 2 Quick Start | [PHASE_2_QUICK_START.md](./PHASE_2_QUICK_START.md) |
| Phase 3 Guide | [PHASE_3_GUIDE.md](./PHASE_3_GUIDE.md) |
| Phase 3 Quick Start | [PHASE_3_QUICK_START.md](./PHASE_3_QUICK_START.md) |
| Phase 3 Completion | [PHASE_3_COMPLETION.md](./PHASE_3_COMPLETION.md) |

---

## 🎉 ACHIEVEMENTS

✅ Complete REST API for library management  
✅ Full entity relationships with cascading  
✅ Advanced search and filtering  
✅ Pagination with metadata  
✅ Sorting by multiple criteria  
✅ Interactive dashboard UI  
✅ 64+ production-ready endpoints  
✅ Comprehensive documentation  
✅ Zero compilation errors  
✅ Ready for Phase 4 development  

---

**Last Updated:** Phase 3 Complete  
**Build Status:** ✅ SUCCESS  
**Next Phase:** Phase 4 - Authentication & Security

Ready to start? Run `mvn spring-boot:run` and visit https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev!

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
