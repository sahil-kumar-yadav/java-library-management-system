# 🚀 QUICK START - PHASE 1

## One-Minute Setup

```bash
# 1. Navigate to project
cd /workspaces/codespaces-blank/library-management-system/library-api

# 2. Start application
mvn spring-boot:run

# 3. In another terminal, test
curl https://studious-lamp-6654w7vjgxv3gp-8080.app.github.dev/api/books
```

**Expected output:** `[]` (empty array)

---

## ✅ Verify It Works

### Test 1: Create a Book
```bash
curl -X POST https://studious-lamp-6654w7vjgxv3gp-8080.app.github.dev/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Clean Code",
    "author": "Robert C. Martin",
    "isbn": "978-0132350884",
    "available": true
  }'
```

**Expected:** `201 CREATED` with book data including generated `id`

### Test 2: Get All Books
```bash
curl https://studious-lamp-6654w7vjgxv3gp-8080.app.github.dev/api/books
```

**Expected:** Array with the book you just created

### Test 3: H2 Database Console
Open browser: https://studious-lamp-6654w7vjgxv3gp-8080.app.github.dev/h2-console
- JDBC URL: `jdbc:h2:mem:librarydb`
- User: `sa`
- Password: (empty)
- Click "Connect" and run: `SELECT * FROM BOOKS;`

---

## 📚 Documentation

| File | Purpose |
|------|---------|
| [README.md](./README.md) | Project overview |
| [PHASE_1_GUIDE.md](./PHASE_1_GUIDE.md) | Detailed concepts & learning |
| [TESTING_GUIDE.md](./TESTING_GUIDE.md) | 25+ test scenarios |
| [PROJECT_SUMMARY.md](./PROJECT_SUMMARY.md) | Complete summary |
| [PHASE_1_COMPLETION.md](./PHASE_1_COMPLETION.md) | Completion checklist |

---

## 🎯 What to Do Next

1. ✅ Run the application (`mvn spring-boot:run`)
2. ✅ Test endpoints (curl or Postman)
3. ✅ Read PHASE_1_GUIDE.md
4. ✅ View database console
5. ✅ Experiment with code
6. ✅ When confident, ask for Phase 2!

---

## 📋 API Quick Reference

### Books
- `GET /api/books` - Get all
- `POST /api/books` - Create
- `GET /api/books/{id}` - Get one
- `PUT /api/books/{id}` - Update
- `DELETE /api/books/{id}` - Delete
- `GET /api/books/search/title?q=...` - Search

### Students
- `GET /api/students` - Get all
- `POST /api/students` - Create
- `GET /api/students/{id}` - Get one
- `PUT /api/students/{id}` - Update
- `DELETE /api/students/{id}` - Delete
- `GET /api/students/search/email?q=...` - Search

---

## 💻 IDE Setup (VS Code)

The project is already set up in `/workspaces/codespaces-blank/library-management-system`

Recommended extensions:
- Extension Pack for Java
- Spring Boot Extension Pack
- REST Client

---

## ⚡ Maven Commands

```bash
# Run application
mvn spring-boot:run

# Build package
mvn clean package

# Run tests
mvn test

# Clean build files
mvn clean
```

---

## 🎉 You're All Set!

Everything is ready to go. The application builds successfully and all endpoints are implemented.

**Next:** Run it and test the endpoints!

```bash
mvn spring-boot:run
```

---

**Questions? Check PHASE_1_GUIDE.md for detailed explanations!**
