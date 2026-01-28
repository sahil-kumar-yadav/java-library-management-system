# Phase 3: Quick Start - Advanced Queries & Pagination

## ⚡ Quick Commands

### 1. Build Phase 3
```bash
cd /workspaces/codespaces-blank/library-management-system/library-api
mvn clean package -DskipTests
```

### 2. Run the Application
```bash
mvn spring-boot:run
```

### 3. Open Dashboard
```
http://localhost:8000
```

## 📌 Test with Sample Data

### Add Sample Books
```bash
curl -X POST http://localhost:8000/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Spring in Action",
    "author": "Craig Walls",
    "isbn": "978-1617299551",
    "available": true
  }'

curl -X POST http://localhost:8000/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Effective Java",
    "author": "Joshua Bloch",
    "isbn": "978-0134685991",
    "available": true
  }'

curl -X POST http://localhost:8000/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Clean Code",
    "author": "Robert Martin",
    "isbn": "978-0132350884",
    "available": false
  }'
```

### Add Sample Students
```bash
curl -X POST http://localhost:8000/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice Johnson",
    "email": "alice@university.edu",
    "rollNumber": "CS001",
    "active": true
  }'

curl -X POST http://localhost:8000/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Bob Smith",
    "email": "bob@university.edu",
    "rollNumber": "CS002",
    "active": true
  }'

curl -X POST http://localhost:8000/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Charlie Brown",
    "email": "charlie@university.edu",
    "rollNumber": "CSE003",
    "active": false
  }'
```

## 🔍 Test Phase 3 Features

### 1. BOOK SEARCH

#### Search by Title or Author (any book with "spring" or "craig")
```bash
curl "http://localhost:8000/api/books/search/query?query=spring"
```

**Expected Response:**
```json
[
  {
    "id": 1,
    "title": "Spring in Action",
    "author": "Craig Walls",
    "isbn": "978-1617299551",
    "available": true
  }
]
```

#### Search by Title AND Author (must match both)
```bash
curl "http://localhost:8000/api/books/search/advanced?title=Spring&author=Craig"
```

#### Search with Pagination
```bash
curl "http://localhost:8000/api/books/search/paginated?query=java&page=0&size=10"
```

**Expected Response:**
```json
{
  "content": [
    {
      "id": 2,
      "title": "Effective Java",
      "author": "Joshua Bloch",
      "isbn": "978-0134685991",
      "available": true
    }
  ],
  "pageNumber": 0,
  "pageSize": 10,
  "totalElements": 1,
  "totalPages": 1,
  "hasNext": false,
  "hasPrevious": false
}
```

#### Get Available Books (with Pagination)
```bash
curl "http://localhost:8000/api/books/available/paginated?page=0&size=10"
```

#### Get Unavailable Books (with Pagination)
```bash
curl "http://localhost:8000/api/books/unavailable/paginated?page=0&size=10"
```

#### Sort Books by Title (Ascending A-Z)
```bash
curl "http://localhost:8000/api/books/sorted/title?asc=true&page=0&size=10"
```

**Expected Response Shows Books in Order:**
```json
{
  "content": [
    {
      "id": 3,
      "title": "Clean Code",
      "author": "Robert Martin",
      "available": false
    },
    {
      "id": 2,
      "title": "Effective Java",
      "author": "Joshua Bloch",
      "available": true
    },
    {
      "id": 1,
      "title": "Spring in Action",
      "author": "Craig Walls",
      "available": true
    }
  ],
  "pageNumber": 0,
  "pageSize": 10,
  "totalElements": 3,
  "totalPages": 1,
  "hasNext": false,
  "hasPrevious": false
}
```

#### Sort Books by Title (Descending Z-A)
```bash
curl "http://localhost:8000/api/books/sorted/title?asc=false&page=0&size=10"
```

#### Sort Books by Author
```bash
curl "http://localhost:8000/api/books/sorted/author?asc=true&page=0&size=10"
```

---

### 2. STUDENT SEARCH

#### Search by Name or Email
```bash
curl "http://localhost:8000/api/students/search/query?query=alice"
```

**Expected Response:**
```json
[
  {
    "id": 1,
    "name": "Alice Johnson",
    "email": "alice@university.edu",
    "rollNumber": "CS001",
    "active": true
  }
]
```

#### Search by Roll Number Pattern (starts with "CS")
```bash
curl "http://localhost:8000/api/students/search/rollpattern?pattern=CS"
```

**Expected Response:** Alice (CS001) and Bob (CS002) but NOT Charlie (CSE003)
```json
[
  {
    "id": 1,
    "name": "Alice Johnson",
    "rollNumber": "CS001",
    "active": true
  },
  {
    "id": 2,
    "name": "Bob Smith",
    "rollNumber": "CS002",
    "active": true
  }
]
```

#### Get Active Students (with Pagination)
```bash
curl "http://localhost:8000/api/students/active/paginated?page=0&size=10"
```

#### Get Inactive Students (with Pagination)
```bash
curl "http://localhost:8000/api/students/inactive/paginated?page=0&size=10"
```

#### Search Students with Pagination
```bash
curl "http://localhost:8000/api/students/search/paginated?query=john&page=0&size=10"
```

#### Sort Students by Name (Ascending A-Z)
```bash
curl "http://localhost:8000/api/students/sorted/name?asc=true&page=0&size=10"
```

**Expected Response:**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Alice Johnson",
      "email": "alice@university.edu",
      "rollNumber": "CS001",
      "active": true
    },
    {
      "id": 2,
      "name": "Bob Smith",
      "email": "bob@university.edu",
      "rollNumber": "CS002",
      "active": true
    },
    {
      "id": 3,
      "name": "Charlie Brown",
      "email": "charlie@university.edu",
      "rollNumber": "CSE003",
      "active": false
    }
  ],
  "pageNumber": 0,
  "pageSize": 10,
  "totalElements": 3,
  "totalPages": 1,
  "hasNext": false,
  "hasPrevious": false
}
```

#### Sort Students by Roll Number (Descending Z-A)
```bash
curl "http://localhost:8000/api/students/sorted/rollnumber?asc=false&page=0&size=10"
```

#### Count Active Students
```bash
curl "http://localhost:8000/api/students/active/count"
```

**Expected Response:**
```
2
```

---

### 3. LOAN ADVANCED QUERIES

#### First, Create Some Loans
```bash
# Create loan: Student 1 borrows Book 1
curl -X POST http://localhost:8000/api/loans \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": 1,
    "bookId": 1,
    "dueDate": "2026-02-28T23:59:59"
  }'

# Create loan: Student 2 borrows Book 2
curl -X POST http://localhost:8000/api/loans \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": 2,
    "bookId": 2,
    "dueDate": "2026-03-15T23:59:59"
  }'

# Create loan: Student 1 borrows Book 3
curl -X POST http://localhost:8000/api/loans \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": 1,
    "bookId": 3,
    "dueDate": "2026-01-20T23:59:59"
  }'
```

#### Get Active Loans Sorted by Due Date
```bash
curl "http://localhost:8000/api/loans/active/sorted"
```

**Expected Response:** Loans sorted by dueDate ascending
```json
[
  {
    "id": 3,
    "studentId": 1,
    "bookId": 3,
    "dueDate": "2026-01-20T23:59:59",
    "isReturned": false
  },
  {
    "id": 1,
    "studentId": 1,
    "bookId": 1,
    "dueDate": "2026-02-28T23:59:59",
    "isReturned": false
  },
  {
    "id": 2,
    "studentId": 2,
    "bookId": 2,
    "dueDate": "2026-03-15T23:59:59",
    "isReturned": false
  }
]
```

#### Get Loans Due Between Dates
```bash
curl "http://localhost:8000/api/loans/due/between?startDate=2026-01-28T00:00:00&endDate=2026-03-01T23:59:59"
```

**Expected Response:** Loans 1 and 3 (due within this range)
```json
[
  {
    "id": 3,
    "studentId": 1,
    "bookId": 3,
    "dueDate": "2026-01-20T23:59:59",
    "isReturned": false
  },
  {
    "id": 1,
    "studentId": 1,
    "bookId": 1,
    "dueDate": "2026-02-28T23:59:59",
    "isReturned": false
  }
]
```

#### Get Student's Loans Sorted
```bash
curl "http://localhost:8000/api/loans/student/1/sorted"
```

**Expected Response:** All loans for Student 1, sorted by loan date (newest first)
```json
[
  {
    "id": 3,
    "studentId": 1,
    "bookId": 3,
    "dueDate": "2026-01-20T23:59:59",
    "isReturned": false
  },
  {
    "id": 1,
    "studentId": 1,
    "bookId": 1,
    "dueDate": "2026-02-28T23:59:59",
    "isReturned": false
  }
]
```

#### Count Active Loans for Student
```bash
curl "http://localhost:8000/api/loans/student/1/count-active"
```

**Expected Response:**
```
2
```

#### Get All Loans with Pagination
```bash
curl "http://localhost:8000/api/loans/paginated?page=0&size=10"
```

#### Get Active Loans with Pagination
```bash
curl "http://localhost:8000/api/loans/active/paginated?page=0&size=10"
```

#### Get Overdue Loans with Pagination
```bash
curl "http://localhost:8000/api/loans/overdue/paginated?page=0&size=10"
```

**Note:** Loan is overdue if dueDate < NOW and isReturned = false

#### Get Student's Loans with Pagination
```bash
curl "http://localhost:8000/api/loans/student/1/paginated?page=0&size=10"
```

#### Get Recently Returned Loans
```bash
curl "http://localhost:8000/api/loans/recently-returned"
```

---

## 🎯 Testing Pagination

### Test Pagination with Multiple Pages

#### Step 1: Add 15 Books
```bash
for i in {1..15}; do
  curl -X POST http://localhost:8000/api/books \
    -H "Content-Type: application/json" \
    -d "{\"title\": \"Book $i\", \"author\": \"Author $i\", \"isbn\": \"ISBN$i\", \"available\": true}"
done
```

#### Step 2: Get Page 1 (Size 5)
```bash
curl "http://localhost:8000/api/books/available/paginated?page=0&size=5"
```

**Response Should Show:**
```json
{
  "content": [ /* 5 books */ ],
  "pageNumber": 0,
  "pageSize": 5,
  "totalElements": 15,
  "totalPages": 3,
  "hasNext": true,      // ← Can get next page
  "hasPrevious": false   // ← No previous page
}
```

#### Step 3: Get Page 2
```bash
curl "http://localhost:8000/api/books/available/paginated?page=1&size=5"
```

**Response Should Show:**
```json
{
  "content": [ /* 5 more books */ ],
  "pageNumber": 1,
  "pageSize": 5,
  "totalElements": 15,
  "totalPages": 3,
  "hasNext": true,      // ← Can get next page
  "hasPrevious": true    // ← Can go back
}
```

#### Step 4: Get Page 3 (Last Page)
```bash
curl "http://localhost:8000/api/books/available/paginated?page=2&size=5"
```

**Response Should Show:**
```json
{
  "content": [ /* 5 final books */ ],
  "pageNumber": 2,
  "pageSize": 5,
  "totalElements": 15,
  "totalPages": 3,
  "hasNext": false,     // ← No more pages
  "hasPrevious": true    // ← Can go back
}
```

## 📊 Summary of New Endpoints

| Feature | Endpoint | Method |
|---------|----------|--------|
| **BOOK SEARCH** | | |
| Search by text | `/books/search/query?query=X` | GET |
| Advanced search | `/books/search/advanced?title=X&author=Y` | GET |
| Available books | `/books/available/paginated?page=0&size=10` | GET |
| Unavailable books | `/books/unavailable/paginated?page=0&size=10` | GET |
| Search paginated | `/books/search/paginated?query=X&page=0&size=10` | GET |
| Sort by title | `/books/sorted/title?asc=true&page=0&size=10` | GET |
| Sort by author | `/books/sorted/author?asc=true&page=0&size=10` | GET |
| **STUDENT SEARCH** | | |
| Search by text | `/students/search/query?query=X` | GET |
| Search by pattern | `/students/search/rollpattern?pattern=X` | GET |
| Active students | `/students/active/paginated?page=0&size=10` | GET |
| Inactive students | `/students/inactive/paginated?page=0&size=10` | GET |
| Search paginated | `/students/search/paginated?query=X&page=0&size=10` | GET |
| Sort by name | `/students/sorted/name?asc=true&page=0&size=10` | GET |
| Sort by roll | `/students/sorted/rollnumber?asc=true&page=0&size=10` | GET |
| Count active | `/students/active/count` | GET |
| **LOAN QUERIES** | | |
| Active sorted | `/loans/active/sorted` | GET |
| Due between | `/loans/due/between?startDate=X&endDate=Y` | GET |
| Student loans sorted | `/loans/student/{id}/sorted` | GET |
| Student active count | `/loans/student/{id}/count-active` | GET |
| Recently returned | `/loans/recently-returned` | GET |
| All paginated | `/loans/paginated?page=0&size=10` | GET |
| Active paginated | `/loans/active/paginated?page=0&size=10` | GET |
| Overdue paginated | `/loans/overdue/paginated?page=0&size=10` | GET |
| Student paginated | `/loans/student/{id}/paginated?page=0&size=10` | GET |

## ✅ Phase 3 Testing Checklist

- [ ] Build successful: `mvn clean package`
- [ ] Application runs: `mvn spring-boot:run`
- [ ] Dashboard loads: `http://localhost:8000`
- [ ] Add sample books
- [ ] Add sample students
- [ ] Test book search
- [ ] Test book pagination
- [ ] Test book sorting
- [ ] Test student search
- [ ] Test student pagination
- [ ] Test student counting
- [ ] Create loans
- [ ] Test loan queries
- [ ] Test date range queries
- [ ] Test pagination navigation (hasNext/hasPrevious)

## 🎓 Key Takeaways

**What Phase 3 Teaches:**
1. **Custom JPQL Queries** - Write your own SQL-like queries
2. **Advanced Search** - Search across multiple fields
3. **Pagination** - Display large datasets efficiently
4. **Sorting** - Order results by any field
5. **DTOs** - Custom response objects
6. **Pagination Helpers** - Reusable pagination logic

**Total Endpoints Now:** 64+
- Phase 1: 20 endpoints
- Phase 2: 20 endpoints
- Phase 3: 24 endpoints

**Ready for Phase 4:** Authentication & Security!

