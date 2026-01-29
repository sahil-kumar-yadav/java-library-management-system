# Phase 4 Quick Start - Authentication & Security

## Quick Commands

1) Build & Run
```bash
cd library-api
mvn clean package -DskipTests
mvn spring-boot:run
```

2) Register (example)
```bash
curl -X POST https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/auth/register \
  -H 'Content-Type: application/json' \
  -d '{"username":"librarian","password":"pass","roles":["LIBRARIAN"]}'
```

3) Login
```bash
curl -X POST https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"librarian","password":"pass"}'
```

4) Use Access Token
```bash
curl -H "Authorization: Bearer <accessToken>" https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/books
```

5) Refresh Token
```bash
curl -X POST https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/api/auth/refresh -d "<refreshToken>"
```

## Minimal Tests
- Register a librarian and a student
- Login with librarian and call POST /api/books (should succeed)
- Login as student and call POST /api/books (should be 403)

## Notes
- Console shows debug logs for JWT validation
- H2 console remains available at https://studious-lamp-6654w7vjgxv3gp-8000.app.github.dev/h2-console

