# Phase 4: Authentication & Security (JWT) 🔐

## Overview

Phase 4 adds authentication and basic role-based authorization using JWTs.

Key goals:
- Secure API endpoints with JWT tokens
- Implement user registration and login
- Add role-based access (ROLE_STUDENT, ROLE_LIBRARIAN, ROLE_ADMIN)
- Implement refresh tokens
- Use BCrypt for password hashing

## Components Added

- `User` entity (username, password, roles, optional link to `Student`)
- `Role` enum
- `UserRepository`
- `AuthService` (registration, login, refresh)
- `AuthController` (`/auth/register`, `/auth/login`, `/auth/refresh`)
- `JwtUtil` - JWT creation and validation
- `JwtAuthenticationFilter` - Validates token for each request
- `SecurityConfig` - Spring Security configuration

## Flow: Login & Access

1. User registers via `POST /api/auth/register` with username/password and optional roles
2. User logs in via `POST /api/auth/login` and receives an access token and refresh token
3. Client sends `Authorization: Bearer <accessToken>` with requests
4. Server validates token in `JwtAuthenticationFilter` and sets security context
5. Access to endpoints is controlled via `@PreAuthorize` or URL security rules
6. Refresh token can be sent to `POST /api/auth/refresh` to get a new access token

## Endpoints

- `POST /api/auth/register` - Request: `{ "username":"alice", "password":"pass", "roles":["STUDENT"] }`
- `POST /api/auth/login` - Request: `{ "username":"alice", "password":"pass" }` Response: `{ accessToken, refreshToken, tokenType }
- `POST /api/auth/refresh` - Body: refresh token string; returns new access token

## Security Rules (Current)

- `/api/auth/**` and `/h2-console/**` are public
- All other API endpoints require authentication
- Create/Update/Delete endpoints are restricted to `ROLE_LIBRARIAN` or `ROLE_ADMIN` via `@PreAuthorize`

## Token Tips

- Use short-lived access tokens (default 15 minutes)
- Use longer-lived refresh tokens (default 7 days)
- Keep refresh tokens secure (store in httpOnly cookies or secure storage)

## Testing (cURL)

1) Register user
```bash
curl -X POST http://localhost:8000/api/auth/register \
  -H 'Content-Type: application/json' \
  -d '{"username":"librarian","password":"pass","roles":["LIBRARIAN"]}'
```

2) Login
```bash
curl -X POST http://localhost:8000/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"librarian","password":"pass"}'
```
Response:
```json
{ "accessToken":"...", "refreshToken":"...", "tokenType":"Bearer" }
```

3) Use token
```bash
curl -H "Authorization: Bearer <accessToken>" http://localhost:8000/api/books
```

4) Refresh
```bash
curl -X POST http://localhost:8000/api/auth/refresh -d "<refreshToken>"
```

## Notes and Next Steps

- Production: rotate `jwt.secret` frequently and use a secure key management
- Consider storing refresh tokens in DB or implementing revocation
- Add role enforcement to endpoints as needed
- Add UI login flow and token storage (httpOnly cookies recommended)

