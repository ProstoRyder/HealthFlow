# HealthFlow Backend

HealthFlow is a Spring Boot backend for hospital workflow automation.

## Tech Stack

- Java 21
- Spring Boot
- Spring Data JPA (Hibernate)
- PostgreSQL
- Spring Security + JWT
- Gradle
- Lombok
- Swagger (OpenAPI)
- AWS S3 (avatars)

## Project Structure

- `config`
- `common`
- `domain`
- `dto`
- `repository`
- `service`
- `web`

## Run Locally

1. Create `.env` in project root (example below).
2. Start PostgreSQL.
3. Run:

```bash
./gradlew bootRun
```

App URL: `http://localhost:8080`

Swagger UI: `http://localhost:8080/swagger-ui/index.html`

## Environment Variables (`.env`)

```env
DB_HOST=localhost
DB_PORT=5432
DB_NAME=healthflow_db
DB_USERNAME=postgres
DB_PASSWORD=your_password

JWT_SECRET=change_this_secret_key_to_at_least_32_characters
JWT_EXPIRATION_MINUTES=1440
JWT_REFRESH_EXPIRATION_MINUTES=10080

AWS_REGION=eu-central-1
AWS_ACCESS_KEY=your_access_key
AWS_SECRET_KEY=your_secret_key
AWS_S3_BUCKET=healthflow-avatars

GEMINI_API_KEY=your_gemini_api_key
GEMINI_MODEL=gemini-2.5-flash
GEMINI_BASE_URL=https://generativelanguage.googleapis.com/v1beta
```

## Roles

- `PATIENT`
- `DOCTOR`
- `ADMIN`

## Main Features

- Auth: register/login/refresh/logout
- Role-based access
- Patient self-booking (`SCHEDULED` by default)
- Role-based appointment status updates
- Consultation + prescriptions
- Consultation PDF generation
- Public search for doctors/hospitals/specialties
- Public doctor reviews endpoint
- Avatars upload to AWS S3
- Safe AI assistant endpoint (Gemini, no medical advice)

## Key Endpoints

### Public

- `GET /api/health`
- `GET /api/doctors?q=...`
- `GET /api/doctors/{id}`
- `GET /api/hospitals`
- `GET /api/hospitals/{id}`
- `GET /api/specialties`
- `GET /api/specialties/{id}`
- `GET /api/reviews/doctor/{doctorId}`

### Auth

- `POST /api/auth/register`
- `POST /api/auth/admin/register`
- `POST /api/auth/login`
- `POST /api/auth/refresh`
- `POST /api/auth/logout`

### Appointments

- `POST /api/appointments` (ADMIN, PATIENT)
- `PATCH /api/appointments/{id}/status` (ADMIN, DOCTOR, PATIENT)
- `PUT /api/appointments/{id}` (ADMIN)

### Me Endpoints

- `GET /api/patient/me/profile`
- `GET /api/doctor/me/profile`
- `GET /api/patient/me/consultations/{id}/pdf`
- `GET /api/doctor/me/consultations/{id}/pdf`

### AI

- `POST /api/ai/chat`

## Quick Test Flow

1. Register patient (`/api/auth/register`)
2. Login and get token (`/api/auth/login`)
3. Create/find doctor
4. Patient creates appointment (`/api/appointments`)
5. Doctor marks appointment `COMPLETED` (`PATCH /api/appointments/{id}/status`)
6. Patient creates review (`POST /api/reviews`)
7. Check public doctor reviews (`GET /api/reviews/doctor/{doctorId}`)
8. Ask AI assistant (`POST /api/ai/chat`)

## Notes

- AI assistant is intentionally restricted: no diagnosis, treatment plans, or dosage recommendations.
- If an appointment is `CANCELLED`, the same time slot can be booked again.
