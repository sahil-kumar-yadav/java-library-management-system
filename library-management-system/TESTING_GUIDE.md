# 🧪 PHASE 1: API TESTING GUIDE

## Setup

### 1. Start the Application
```bash
cd library-api
mvn spring-boot:run
```

**Output should show:**
```
Started LibraryApplication in X.XXX seconds (JVM running for X.XXX)
```

### 2. Verify Server is Running
```bash
curl https://studious-lamp-6654w7vjgxv3gp-8080.app.github.dev/api/books
```

Should return: `[]` (empty list)

### 3. Open Postman or API Testing Tool

Use any of:
- **Postman** (GUI recommended for learning)
- **curl** (command line)
- **REST Client** (VS Code extension)
- **Insomnia** (alternative)

---

## 📚 BOOK ENDPOINTS - Complete Test Scenarios

### Test 1: Create First Book
**Endpoint:** `POST /api/books`  
**Content-Type:** `application/json`

**Request Body:**
```json
{
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "isbn": "978-0132350884",
  "available": true
}
```

**Expected Response:** `201 CREATED`
```json
{
  "id": 1,
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "isbn": "978-0132350884",
  "available": true,
  "createdAt": 1674856234567
}
```

**What to notice:**
- Status code is `201 CREATED` (not 200)
- `id` is auto-generated (1)
- `createdAt` is automatic timestamp
- All required fields are present

---

### Test 2: Create Second Book
**Endpoint:** `POST /api/books`

**Request Body:**
```json
{
  "title": "Design Patterns",
  "author": "Gang of Four",
  "isbn": "978-0201633610",
  "available": true
}
```

**Expected Response:** `201 CREATED`
```json
{
  "id": 2,
  "title": "Design Patterns",
  "author": "Gang of Four",
  "isbn": "978-0201633610",
  "available": true,
  "createdAt": 1674856234568
}
```

---

### Test 3: Create Invalid Book (Test Validation)
**Endpoint:** `POST /api/books`

**Request Body (missing title):**
```json
{
  "author": "Someone",
  "isbn": "123456",
  "available": true
}
```

**Expected Response:** `400 BAD REQUEST`

**What to notice:**
- Service validation catches missing title
- Returns 400 (Bad Request) status
- Request is rejected before reaching database

---

### Test 4: Get All Books
**Endpoint:** `GET /api/books`  
**Method:** GET  
**No request body needed**

**Expected Response:** `200 OK`
```json
[
  {
    "id": 1,
    "title": "Clean Code",
    "author": "Robert C. Martin",
    "isbn": "978-0132350884",
    "available": true,
    "createdAt": 1674856234567
  },
  {
    "id": 2,
    "title": "Design Patterns",
    "author": "Gang of Four",
    "isbn": "978-0201633610",
    "available": true,
    "createdAt": 1674856234568
  }
]
```

**What to notice:**
- Returns array of all books
- Status code is 200 (OK)
- Both books created earlier are returned

---

### Test 5: Get Book by ID
**Endpoint:** `GET /api/books/1`

**Expected Response:** `200 OK`
```json
{
  "id": 1,
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "isbn": "978-0132350884",
  "available": true,
  "createdAt": 1674856234567
}
```

---

### Test 6: Get Non-Existent Book (Test Error Handling)
**Endpoint:** `GET /api/books/999`

**Expected Response:** `404 NOT FOUND`
```
(empty body)
```

**What to notice:**
- Returns 404 for non-existent resource
- No error message in body (pure REST)
- Status code clearly indicates the problem

---

### Test 7: Update Book
**Endpoint:** `PUT /api/books/1`

**Request Body:**
```json
{
  "available": false,
  "title": "Clean Code (2nd Edition)"
}
```

**Expected Response:** `200 OK`
```json
{
  "id": 1,
  "title": "Clean Code (2nd Edition)",
  "author": "Robert C. Martin",
  "isbn": "978-0132350884",
  "available": false,
  "createdAt": 1674856234567
}
```

**What to notice:**
- Only provided fields are updated
- Other fields retain their values
- Returns 200 (OK)
- Book is now marked as unavailable

---

### Test 8: Delete Book
**Endpoint:** `DELETE /api/books/1`

**Expected Response:** `204 NO CONTENT`
```
(empty body)
```

**What to notice:**
- Returns 204 (No Content)
- No response body
- Book is deleted from database

---

### Test 9: Verify Deletion
**Endpoint:** `GET /api/books/1`

**Expected Response:** `404 NOT FOUND`
```
(empty body)
```

**What to notice:**
- Book with ID 1 is gone
- Trying to fetch it returns 404

---

### Test 10: Search by Title
**Endpoint:** `GET /api/books/search/title?q=Design`

**Expected Response:** `200 OK`
```json
[
  {
    "id": 2,
    "title": "Design Patterns",
    "author": "Gang of Four",
    "isbn": "978-0201633610",
    "available": true,
    "createdAt": 1674856234568
  }
]
```

**What to notice:**
- Query parameter: `?q=Design`
- Search is case-insensitive
- Returns array of matching books

---

### Test 11: Search by Author
**Endpoint:** `GET /api/books/search/author?q=Gang`

**Expected Response:** `200 OK`
```json
[
  {
    "id": 2,
    "title": "Design Patterns",
    "author": "Gang of Four",
    "isbn": "978-0201633610",
    "available": true,
    "createdAt": 1674856234568
  }
]
```

---

### Test 12: Get Available Books Only
**Endpoint:** `GET /api/books/available`

**Expected Response:** `200 OK`
```json
[
  {
    "id": 2,
    "title": "Design Patterns",
    "author": "Gang of Four",
    "isbn": "978-0201633610",
    "available": true,
    "createdAt": 1674856234568
  }
]
```

**What to notice:**
- Filters books where `available = true`
- Book 1 is not returned (was set to unavailable)

---

### Test 13: Get Borrowed Books
**Endpoint:** `GET /api/books/borrowed`

**Expected Response:** `200 OK`
```json
[] 
// Empty because we deleted book 1
// If book 1 still existed with available=false, it would be here
```

---

### Test 14: Get Count of Books
**Endpoint:** `GET /api/books/count`

**Expected Response:** `200 OK`
```
1
```

**What to notice:**
- Returns a number (not JSON object)
- Only 1 book left (we deleted one)

---

## 👥 STUDENT ENDPOINTS - Complete Test Scenarios

### Test 15: Create First Student
**Endpoint:** `POST /api/students`

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "rollNumber": "CS001",
  "active": true
}
```

**Expected Response:** `201 CREATED`
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "rollNumber": "CS001",
  "active": true,
  "createdAt": 1674856234569
}
```

---

### Test 16: Try Duplicate Email (Test Validation)
**Endpoint:** `POST /api/students`

**Request Body:**
```json
{
  "name": "Jane Doe",
  "email": "john@example.com",
  "rollNumber": "CS002",
  "active": true
}
```

**Expected Response:** `400 BAD REQUEST`

**What to notice:**
- Email is unique
- Service validation prevents duplicates
- Returns 400 error

---

### Test 17: Create Valid Second Student
**Endpoint:** `POST /api/students`

**Request Body:**
```json
{
  "name": "Jane Smith",
  "email": "jane@example.com",
  "rollNumber": "CS002",
  "active": true
}
```

**Expected Response:** `201 CREATED`

---

### Test 18: Get All Students
**Endpoint:** `GET /api/students`

**Expected Response:** `200 OK`
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "rollNumber": "CS001",
    "active": true,
    "createdAt": 1674856234569
  },
  {
    "id": 2,
    "name": "Jane Smith",
    "email": "jane@example.com",
    "rollNumber": "CS002",
    "active": true,
    "createdAt": 1674856234570
  }
]
```

---

### Test 19: Get Student by Email
**Endpoint:** `GET /api/students/search/email?q=john@example.com`

**Expected Response:** `200 OK`
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "rollNumber": "CS001",
  "active": true,
  "createdAt": 1674856234569
}
```

---

### Test 20: Get Student by Roll Number
**Endpoint:** `GET /api/students/search/rollnumber?q=CS002`

**Expected Response:** `200 OK`
```json
{
  "id": 2,
  "name": "Jane Smith",
  "email": "jane@example.com",
  "rollNumber": "CS002",
  "active": true,
  "createdAt": 1674856234570
}
```

---

### Test 21: Update Student
**Endpoint:** `PUT /api/students/1`

**Request Body:**
```json
{
  "name": "John Doe Updated"
}
```

**Expected Response:** `200 OK`
```json
{
  "id": 1,
  "name": "John Doe Updated",
  "email": "john@example.com",
  "rollNumber": "CS001",
  "active": true,
  "createdAt": 1674856234569
}
```

---

### Test 22: Deactivate Student
**Endpoint:** `PUT /api/students/2`

**Request Body:**
```json
{
  "active": false
}
```

**Expected Response:** `200 OK`

---

### Test 23: Get Active Students Only
**Endpoint:** `GET /api/students/active`

**Expected Response:** `200 OK`
```json
[
  {
    "id": 1,
    "name": "John Doe Updated",
    "email": "john@example.com",
    "rollNumber": "CS001",
    "active": true,
    "createdAt": 1674856234569
  }
]
```

**What to notice:**
- Only 1 student (Jane is inactive)

---

### Test 24: Delete Student
**Endpoint:** `DELETE /api/students/2`

**Expected Response:** `204 NO CONTENT`

---

### Test 25: Verify Student Deleted
**Endpoint:** `GET /api/students/2`

**Expected Response:** `404 NOT FOUND`

---

## 📊 H2 DATABASE CONSOLE

### Access Database UI
1. Open: https://studious-lamp-6654w7vjgxv3gp-8080.app.github.dev/h2-console
2. JDBC URL: `jdbc:h2:mem:librarydb`
3. Username: `sa`
4. Password: (leave empty)
5. Click "Connect"

### Query Tables

**View all books:**
```sql
SELECT * FROM BOOKS;
```

**View all students:**
```sql
SELECT * FROM STUDENTS;
```

**Count books:**
```sql
SELECT COUNT(*) FROM BOOKS;
```

**Find available books:**
```sql
SELECT * FROM BOOKS WHERE AVAILABLE = true;
```

---

## 🔍 COMMON TESTING ISSUES

### Issue 1: Port 8080 Already in Use
```bash
# Kill process on port 8080 (macOS/Linux)
lsof -ti:8080 | xargs kill -9

# Or change port in application.properties
server.port=8081
```

### Issue 2: Database Already Has Data
- Data persists during the same run
- Restart the application to clear H2 (in-memory)
- Data is lost when application stops

### Issue 3: JSON Format Error in Postman
- Ensure Content-Type is `application/json`
- Check for trailing commas in JSON
- Validate JSON syntax

### Issue 4: Getting Different Results
- Make sure you're testing in order
- Each test builds on previous ones
- Restart app if confused about state

---

## ✅ TEST CHECKLIST

- [ ] Create Book successfully (201 CREATED)
- [ ] Create invalid book gets rejected (400 BAD REQUEST)
- [ ] Get all books returns list (200 OK)
- [ ] Get book by ID works (200 OK)
- [ ] Non-existent book returns 404
- [ ] Update book partially (200 OK)
- [ ] Delete book works (204 NO CONTENT)
- [ ] Search by title works
- [ ] Search by author works
- [ ] Get available books filters correctly
- [ ] Get count returns number
- [ ] Create student successfully (201 CREATED)
- [ ] Duplicate email rejected (400 BAD REQUEST)
- [ ] Get all students works
- [ ] Get student by email works
- [ ] Get student by roll number works
- [ ] Update student works
- [ ] Deactivate student works
- [ ] Get active students filters correctly
- [ ] Delete student works (204 NO CONTENT)
- [ ] H2 console queries work

---

## 🎓 LEARNING OUTCOMES

After running these tests, you understand:

✅ **HTTP Status Codes:**
- 201 = Created
- 200 = OK
- 204 = No Content
- 400 = Bad Request (client error)
- 404 = Not Found

✅ **REST Principles:**
- Resource-based URLs
- Standard HTTP methods
- Proper status codes
- JSON format

✅ **API Design:**
- Clean endpoints
- Consistent naming
- Proper error handling
- Search/filter capabilities

✅ **Database Operations:**
- CRUD operations work correctly
- Data persists and retrieves
- Validation prevents bad data
- Relationships will come in Phase 2

---

**You're now ready to explore the codebase and proceed to Phase 2!** 🎉
