# Phase 2 Quick Start Guide

## 🚀 Getting Started in 2 Minutes

### 1. Start the App
```bash
cd /workspaces/codespaces-blank/library-management-system/library-api
mvn spring-boot:run
```

Wait 10 seconds for startup...

### 2. Open Dashboard
```
http://localhost:8080/index.html
```

### 3. Try It Out!

#### Step 1: Add a Book
1. Click **Books** tab
2. Enter:
   - Title: "Clean Code"
   - Author: "Robert C. Martin"
   - ISBN: "978-0132350884"
3. Click **Add Book**

#### Step 2: Add a Student
1. Click **Students** tab
2. Enter:
   - Name: "Alex Kumar"
   - Email: "alex@university.edu"
   - Roll Number: "CS2024001"
3. Click **Add Student**

#### Step 3: Create a Loan
1. Click **Loans** tab
2. Select student: "Alex Kumar"
3. Select book: "Clean Code"
4. Click **Create Loan**
5. ✅ Book is now borrowed! Status changes to "Borrowed"

#### Step 4: Return the Book
1. Still in **Loans** tab
2. Find the loan you created
3. Click **Return**
4. ✅ Book is now available again!

#### Step 5: Try Reservations
1. Add another student and another book
2. Click **Reservations** tab
3. Create a reservation
4. See queue position #1

## 📊 Dashboard Statistics

The **Dashboard** tab shows:
- Total Books
- Total Students  
- Active Loans (not returned)
- Active Reservations (not cancelled)

Updates automatically as you create/return items!

## 🔗 API Examples (cURL)

### List All Books
```bash
curl http://localhost:8080/api/books | jq .
```

### Create Book
```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Spring in Action",
    "author": "Craig Walls",
    "isbn": "978-1617294945"
  }'
```

### Create Student
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sarah Ahmed",
    "email": "sarah@example.com",
    "rollNumber": "CS2024002"
  }'
```

### Create Loan (borrow a book)
```bash
curl -X POST "http://localhost:8080/api/loans?studentId=1&bookId=1"
```

### View All Active Loans
```bash
curl http://localhost:8080/api/loans/active | jq .
```

### Return a Book
```bash
curl -X PUT http://localhost:8080/api/loans/1/return
```

### View Student's Loans
```bash
curl http://localhost:8080/api/loans/student/1 | jq .
```

### Check for Overdue Loans
```bash
curl http://localhost:8080/api/loans/overdue | jq .
```

### Create Reservation
```bash
curl -X POST "http://localhost:8080/api/reservations?studentId=1&bookId=1"
```

### View Reservation Queue for a Book
```bash
curl http://localhost:8080/api/reservations/book/1/active | jq .
```

### Cancel Reservation
```bash
curl -X PUT http://localhost:8080/api/reservations/1/cancel
```

## 🎯 Common Workflows

### Workflow 1: Borrow a Book
```
1. Student selects book
2. API: POST /api/loans?studentId=X&bookId=Y
3. Book.available becomes false
4. Loan record created with today's date + 14 days
```

### Workflow 2: Return a Book
```
1. Student returns book
2. API: PUT /api/loans/{id}/return
3. Loan.isReturned = true
4. Loan.returnDate = today
5. Book.available = true (available for others)
```

### Workflow 3: Reserve Unavailable Book
```
1. Book is borrowed (available = false)
2. Student creates reservation
3. API: POST /api/reservations?studentId=X&bookId=Y
4. Gets queue position #1 (or higher if others waiting)
5. Waits for book to be returned
```

### Workflow 4: Cancel Reservation
```
1. Student cancels their reservation
2. API: PUT /api/reservations/{id}/cancel
3. Reservation.isCancelled = true
4. Other reservations' queue positions update automatically
```

## 📋 Response Examples

### Create Loan Response
```json
{
  "id": 1,
  "student": {
    "id": 1,
    "name": "Alex Kumar",
    "email": "alex@university.edu",
    "rollNumber": "CS2024001",
    "active": true,
    "createdAt": 1611234567890
  },
  "book": {
    "id": 1,
    "title": "Clean Code",
    "author": "Robert C. Martin",
    "isbn": "978-0132350884",
    "available": false,
    "createdAt": 1611234567890
  },
  "loanDate": "2026-01-28T12:00:00",
  "dueDate": "2026-02-11T12:00:00",
  "returnDate": null,
  "isReturned": false,
  "createdAt": "2026-01-28T12:00:00"
}
```

### Create Reservation Response
```json
{
  "id": 1,
  "student": {
    "id": 2,
    "name": "Sarah Ahmed",
    "email": "sarah@example.com",
    "rollNumber": "CS2024002",
    "active": true,
    "createdAt": 1611234567890
  },
  "book": {
    "id": 1,
    "title": "Clean Code",
    "author": "Robert C. Martin",
    "isbn": "978-0132350884",
    "available": false,
    "createdAt": 1611234567890
  },
  "reservationDate": "2026-01-28T12:30:00",
  "cancelledDate": null,
  "isCancelled": false,
  "queuePosition": 1,
  "createdAt": "2026-01-28T12:30:00"
}
```

## ⚡ Error Scenarios

### Error: "Book is not available for borrowing"
**Cause:** Book.available = false (already borrowed)
**Solution:** Return it first or create a reservation

### Error: "Student already has this book borrowed"
**Cause:** Same student trying to borrow same book twice
**Solution:** Return the first loan first

### Error: "Student already has a reservation for this book"
**Cause:** Duplicate reservation
**Solution:** Cancel existing reservation first

### Error: "Book is already returned"
**Cause:** Trying to return a loan that's already returned
**Solution:** Check if loan was returned before

## 🔍 Useful Queries

### Get stats for dashboard
```bash
# Total books
curl http://localhost:8080/api/books/count

# Total students
curl http://localhost:8080/api/students/count

# Active loans
curl http://localhost:8080/api/loans/active | jq 'length'

# Active reservations
curl http://localhost:8080/api/reservations/active | jq 'length'
```

### Monitor specific student
```bash
# Get all loans for student ID 1
curl http://localhost:8080/api/loans/student/1 | jq .

# Get active loans only
curl http://localhost:8080/api/loans/student/1/active | jq .

# Get reservations
curl http://localhost:8080/api/reservations/student/1 | jq .
```

### Book tracking
```bash
# All loans for book ID 1
curl http://localhost:8080/api/loans/book/1 | jq .

# Current reservation queue
curl http://localhost:8080/api/reservations/book/1/active | jq .
```

## 🎓 What You're Learning

- **Database Relationships** (@OneToMany, @ManyToOne)
- **JPA Entities** (mapping objects to database tables)
- **Service Layer** (business logic and validation)
- **REST APIs** (HTTP endpoints)
- **Transaction Management** (@Transactional)
- **Frontend Integration** (JavaScript with APIs)
- **State Management** (tracking loan/reservation status)

## 💾 Data Persistence

All data is stored in **H2 in-memory database**:
```
Location: jdbc:h2:mem:librarydb
```

**Note:** Data is lost when app restarts. This is perfect for learning!

In Phase 3+, we'll add:
- Persistent MySQL/PostgreSQL database
- User authentication
- Advanced queries with pagination

## ❓ Troubleshooting

### App won't start?
```bash
# Kill any existing processes
pkill -f "java.*LibraryApplication"

# Clean build
mvn clean install -DskipTests

# Run
mvn spring-boot:run
```

### Dashboard not loading?
```bash
# Check if app is running
curl http://localhost:8080/api/books

# Should return: [] (empty array) or [{ book objects }]
```

### Endpoints returning 404?
```bash
# Check app is running on port 8080
netstat -an | grep 8080

# Try directly
curl http://localhost:8080/api/loans
```

## 🎉 Next Steps

1. **Explore the UI** - Try all tabs and features
2. **Test with cURL** - Use API examples above
3. **Create realistic data** - Build a sample library
4. **Read PHASE_2_GUIDE.md** - Deep dive into concepts
5. **Prepare for Phase 3** - Authentication, advanced queries

---

**Phase 2 = Database Relationships Complete!** ✅
