# EMS – Employee Management System

A Spring Boot REST API for managing employees, departments, leaves, employee files (photo/document uploads), and bulk employee import — built with Spring Boot 4.1.1, Spring Data JPA, Spring Security, Spring Batch, Redis caching, and Spring Doc (OpenAPI/Swagger).

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.1.1 |
| Web | Spring MVC (`spring-boot-starter-webmvc`) |
| Persistence | Spring Data JPA + Hibernate |
| Database | MySQL |
| Security | Spring Security (basic auth) |
| Caching | Spring Cache + Redis |
| Batch Processing | Spring Batch (CSV import) |
| Validation | Jakarta Bean Validation (`spring-boot-starter-validation`) |
| API Docs | springdoc-openapi (Swagger UI) |
| Monitoring | Spring Boot Actuator |
| Boilerplate reduction | Lombok |
| Build Tool | Maven (with Maven Wrapper) |

---

## Features

- **Employee Management** — Create, read, update, partially update, and delete employees (`/api/v1`), plus a v2 API with department assignment at creation time (`/api/v2`).
- **File Upload** — Create an employee along with a photo/document in a single multipart request.
- **Pagination & Search** — Paginated, sortable, and searchable employee and leave listings.
- **N+1 Optimization** — A dedicated optimized endpoint for fetching all employees.
- **Department Management** — Create departments and assign them to employees.
- **Leave Management** — Apply for leave, approve/reject leave, list leaves (paginated/searchable), and delete a leave.
- **Bulk Import (Spring Batch)** — Import employees in bulk from `employees.csv` via a chunk-oriented batch job.
- **Auditing** — `createdBy` / `createdAt` / `updatedBy` / `updatedAt` auto-populated on every entity via `@EnableJpaAuditing`.
- **Global Exception Handling** — Centralized `@RestControllerAdvice` mapping domain exceptions (e.g. `EmployeeNotFoundException`, `EmailNotFoundException`) to a uniform `ResponseStructureDto`.
- **Request Logging Filter** — Logs method, URI, client IP, response status, and execution time for every request.
- **Caching** — Redis-backed response caching (1-minute TTL, JSON serialization).
- **Security** — Basic-auth protected endpoints (default admin user).
- **API Documentation** — Swagger UI available out of the box.
- **Actuator** — Health, info, metrics, beans, and mappings endpoints exposed.

---

## Project Structure

```
EMS/
├── src/main/java/com/tyss/EMS/
│   ├── EmsApplication.java          # Main entry point
│   ├── config/                      # Auditing, Redis, Spring Batch config
│   ├── controller/                  # REST controllers (Employee v1/v2, Department, Leave, Batch)
│   ├── dto/                         # Request/response DTOs
│   ├── entity/                      # JPA entities (Employee, Department, Leave, EmployeeFile, Address)
│   ├── exception/                   # Custom exceptions + GlobalExceptionHandler
│   ├── filter/                      # LoggingFilter (request/response logging)
│   ├── repository/                  # Spring Data JPA repositories
│   ├── service/                     # Service interfaces + implementations
│   └── util/                        # Entity <-> DTO mappers
├── src/main/resources/
│   ├── application.properties       # DB, Redis, security, logging, actuator config
│   └── uploads/employees.csv        # Sample CSV for batch import
├── src/test/java/com/tyss/EMS/
│   └── EmsApplicationTests.java
├── pom.xml
├── mvnw / mvnw.cmd
└── README.md                        # ← this file goes here
```

---

## Prerequisites

- **Java 21**
- **Maven** (or use the bundled `mvnw` / `mvnw.cmd` wrapper — no local Maven install needed)
- **MySQL** server running locally (or update the connection URL)
- **Redis** server running locally (used for `@Cacheable` responses)

---

## Configuration

All configuration lives in `src/main/resources/application.properties`. Update these before running:

```properties
# MySQL Database
spring.datasource.url=jdbc:mysql://localhost:3306/EMS_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=Root

# Spring Security (Basic Auth)
spring.security.user.name=admin
spring.security.user.password=admin123
spring.security.user.roles=ADMIN

# Redis
spring.data.redis.host=localhost
spring.data.redis.port=6379
```

> ⚠️ The DB username/password and admin credentials are hardcoded for local development. For any shared or production environment, externalize these via environment variables or a `application-{profile}.properties` file, and never commit real credentials.

`spring.jpa.hibernate.ddl-auto=update` means Hibernate will auto-create/update tables on startup — no manual schema setup needed for a fresh DB.

---

## Running the Application

```bash
# 1. Clone / open the project, then from the project root (where pom.xml is):

# 2. Make sure MySQL and Redis are running locally

# 3. Run with the Maven wrapper
./mvnw spring-boot:run          # macOS/Linux
mvnw.cmd spring-boot:run        # Windows

# The app starts on the default port: http://localhost:8080
```

Basic-auth is enabled by default — use `admin` / `admin123` (as configured) for any endpoint that requires authentication.

---

## API Overview

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/employee` | Create an employee |
| GET | `/api/v1/employee/{id}` | Get employee by ID |
| GET | `/api/v1/employees` | Get all employees |
| GET | `/api/v1/employees/optimized` | Get all employees (N+1 optimized) |
| PUT | `/api/v1/employee/{id}` | Full update of an employee |
| PATCH | `/api/v1/employee/{id}` | Partial update of an employee |
| DELETE | `/api/v1/employee/{id}` | Delete an employee |
| POST | `/api/v1/employees/with-photo` | Create employee + upload photo (multipart) |
| POST | `/api/v2/employee` | Create employee (v2, with department assignment) |
| GET | `/api/v2/employees/page` | Paginated/searchable employee listing |
| POST | `/department` | Create a department |
| PUT | `/department/{deptId}/employee/{empId}` | Assign a department to an employee |
| POST | `/api/v1/leave` | Apply for leave |
| GET | `/api/v1/leave` | List leaves (paginated/searchable) |
| PUT | `/api/v1/leave/{id}/action` | Approve/reject a leave |
| DELETE | `/api/v1/leave/{id}/emp/{empId}` | Delete a leave |
| POST | `/employees/import` | Trigger Spring Batch CSV import |

Full interactive documentation is available via **Swagger UI** once the app is running:
```
http://localhost:8080/swagger-ui.html
```

---

## Actuator Endpoints

Exposed at `/actuator/*`: `health`, `info`, `metrics`, `beans`, `mappings`.

```
http://localhost:8080/actuator/health
```

---

## Where to Add This README

Place this file at the **project root** — the same directory as `pom.xml`, `mvnw`, and the `src/` folder:

```
EMS/EMS/README.md   ← put it here (sibling of pom.xml)
```

Your uploaded zip had a nested `EMS/EMS/...` structure (an outer wrapper folder containing the actual Maven project). Make sure the README sits in the **inner** `EMS` folder — the one that directly contains `pom.xml`, `mvnw`, `src/`, and `.git/` — not the outer wrapper folder. That inner folder is also the Git repository root (it's already linked to `origin: github.com/arup23168-cyber/EMS_SpringBoot.git`), and GitHub/GitLab automatically render `README.md` when it's placed at that repo root.
